#!/usr/bin/python3

import threading
import socket
import queue
from resource import Status
from Crypto.PublicKey import RSA
from Crypto.Signature import pkcs1_15
from Crypto.Hash import SHA256

class Sender(threading.Thread):
	def __init__(self, uid, sckt, multicastGroup, resources, peerList, peerMutex, keyPair, commandQueue):
		self.uid = uid.encode('ascii')
		self.sckt = sckt
		self.multicastGroup = multicastGroup
		self.resources = resources
		self.peerList = peerList
		self.keyPair = keyPair
		self.peerMutex = peerMutex
		self.commandQueue = commandQueue
		self.signer = pkcs1_15.PKCS115_SigScheme(self.keyPair['private'])
		self.shouldRun = True
		super().__init__()
		
	def sendMessage(self, message):
		self.sckt.sendto(self.uid + b',' + self.signer.sign(SHA256.new(message)) + message, self.multicastGroup)

	def run(self):
		self.sckt.sendto(self.uid + b',JOIN,' + self.keyPair['public'].exportKey(), self.multicastGroup)
		peerCount = 0
		while self.shouldRun or not self.commandQueue.empty():
			# Check new commands
			try: 
				cmd = self.commandQueue.get(block=False)
				# Execute the command
				if cmd.startswith("QUIT"):
					self.sendMessage(b"LEAVE")

				elif cmd.startswith("WANT"):
					rid = cmd[4]
					if int(rid) >= len(self.peerList) or not rid.isdigit():
						print("Error: resource requested does not exist.")
					else:
						self.sendMessage(b'WANT,' + rid.encode('ascii'))

				elif cmd.startswith("RELEASE"):
					pass
					# @todo send release message

			except queue.Empty:
				pass

			# Check new peers
			with self.peerMutex:
				if peerCount < len(self.peerList):
					# New peer detected, send own public key for him
					self.sckt.sendto(self.uid + b',ADDLIST,' + self.keyPair['public'].exportKey(), self.multicastGroup)
				if peerCount != len(self.peerList):
					# Peer count is wrong, update it. If it was lower, should have sent pub key
					peerCount = len(self.peerList)
					
	def stop(self):
		self.shouldRun = False

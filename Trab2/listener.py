#!/usr/bin/python3

import threading
import socket
from resource import Status
from Crypto.PublicKey import RSA
from Crypto.Signature import pkcs1_15
from Crypto.Hash import SHA256
from time import time

class Listener(threading.Thread):
	def __init__(self, uid, sckt, resources, peerList, peerMutex, keyPair, commandQueue):
		self.uid = uid.encode('ascii')
		self.sckt = sckt
		self.resources = resources
		self.peerList = peerList
		self.keyPair = keyPair
		self.peerMutex = peerMutex
		self.shouldRun = True
		self.commandQueue = commandQueue
		super().__init__()
		
	def verifyMessage(self, peerVerifier, message, signature):
		try:
			peerVerifier.verify(SHA256.new(message), signature)
		except ValueError:
			return False
		return True

	def run(self):
		while self.shouldRun:
			try:
				data, address = self.sckt.recvfrom(1024)
				cid, toVerify = data.split(b',', 1)
				
				if cid != self.uid: # Check if it isn't myself
					# Identify the peer in the peerList first
					peer = None
					for p in self.peerList:
						if p[0] == cid:
							peer = p

					if toVerify.startswith(b'JOIN') or toVerify.startswith(b'ADDLIST'):
						cmd, *args = toVerify.split(b',')
					elif not self.verifyMessage(peer[1], toVerify[256:], toVerify[:256]):
						print("Could not verify command from {}, someone is doing something fishy".format(cid.encode('ascii')))
						continue
					else:
						cmd, *args = toVerify[256:].split(b',')

					# Check if the message is a new join
					if cmd == b'JOIN':
						print("New Peer '{}'{}, adding to peer list".format(cid.decode('ascii'), address))
						with self.peerMutex:
							self.peerList.append((cid, pkcs1_15.PKCS115_SigScheme(RSA.importKey(args[0]))))
			
					# Check for messages from existing peers
					elif cmd == b'ADDLIST':
						# Check if I don't already have the peer
						if cid not in [x for x,__ in self.peerList]:
							print("New existing Peer '{}'{}, adding to peer list".format(cid.decode('ascii'), address))
							with self.peerMutex:
								self.peerList.append((cid, pkcs1_15.PKCS115_SigScheme(RSA.importKey(args[0]))))
				
					# Check for leavers
					elif cmd == b'LEAVE':
						print("Peer '{}' leaving, removing from peer list".format(cid.decode('ascii')))
						for peer in self.peerList:
							if peer[0] == cid:
								self.peerList.remove(peer)

					# Check for resource messages
					elif cmd == b'WANT':
						print("Peer '{}' requesting resource".format(cid.decode('ascii')))
						if self.resources[int(args[0])].requested(cid, int(time())):
							self.commandQueue.put('OK,' + args[0])
						else:
							self.commandQueue.put('NO,' + args[0])

					elif cmd == b'RELEASE':
						# if cid in self.resources[int(args[0])].wantedQueue:
						if cid == self.uid:
							self.resources[int(args[0])].release()
						else:
							# If other peer release and I want, ask it again
							if self.resources[args[0]].status == Status.WANTED:
								self.commandQueue.put(str(self.uid) + 'WANT' + args[0])

			except socket.timeout:
				pass

		# Out of while loop, release and quit
		for resource in self.resources:
			if resource.release():
				self.commandQueue.put()
							
	def stop(self):
		self.shouldRun = False
						

#!/usr/bin/python3

import threading
import socket
import queue


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
		super().__init__()

	def run(self):
		self.sckt.sendto(self.uid + b',JOIN,' + self.keyPair['public'].exportKey(), self.multicastGroup)
		peerCount = 0
		while True:
			# Check new commands
			try: 
				cmd = self.commandQueue.get(block=False)
				# Execute the command
#				if cmd.startswith("GET"):
#					if cmd[3] == "1":
#						self.sckt.sendto(b'Galera, o recurso 1 eh meu', self.multicastGroup)
			except queue.Empty:
				pass

			# Check new peers
			with self.peerMutex:
				if peerCount != len(self.peerList):
					# New peer detected, send own public key for him
					self.sckt.sendto(self.uid + b',ADDLIST,' + self.keyPair['public'].exportKey(), self.multicastGroup)
					peerCount = len(self.peerList)
					
			

#!/usr/bin/python3

import threading
import socket
from Crypto.PublicKey import RSA

class Listener(threading.Thread):
	def __init__(self, sckt, resources, peerList, peerMutex, keyPair):
		self.sckt = sckt
		self.resources = resources
		self.peerList = peerList
		self.keyPair = keyPair
		self.peerMutex = peerMutex
		super().__init__()

	def run(self):
		while True:
			data, address = self.sckt.recvfrom(1024)
			if data.split(b':')[1] != self.keyPair['public'].exportKey(): # Check if it isn't myself
				# Check if the message is a new join
				if data.startswith(b'JOIN:'):
					print("New Peer {}, adding to peer list".format(address))
					with self.peerMutex:
						self.peerList.append(RSA.importKey(data[5:]))
			
				# Check for messages from existing peers
				elif data.startswith(b'ADDLIST:'):
					# Check if I don't already have the peer
					if data[8:] not in [x.exportKey() for x in self.peerList]:
						print("Existing Peer {} sent key, adding to peer list".format(address))
						self.peerList.append(RSA.importKey(data[8:]))

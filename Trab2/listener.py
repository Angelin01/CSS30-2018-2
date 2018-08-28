#!/usr/bin/python3

import threading
import socket
from Crypto.PublicKey import RSA

class Listener(threading.Thread):
	def __init__(self, uid, sckt, resources, peerList, peerMutex, keyPair):
		self.uid = uid.encode('ascii')
		self.sckt = sckt
		self.resources = resources
		self.peerList = peerList
		self.keyPair = keyPair
		self.peerMutex = peerMutex
		super().__init__()

	def run(self):
		while True:
			data, address = self.sckt.recvfrom(1024)
			cid, cmd, *args = data.split(b',')
			if cid != self.uid: # Check if it isn't myself
				# Check if the message is a new join
				if cmd == b'JOIN':
					print("New Peer '{}'{}, adding to peer list".format(cid.decode('ascii'), address))
					with self.peerMutex:
						self.peerList.append((cid, RSA.importKey(args[0])))
			
				# Check for messages from existing peers
				elif cmd == b'ADDLIST':
					# Check if I don't already have the peer
					if cid not in [x for x,__ in self.peerList]:
						print("New existing Peer '{}'{}, adding to peer list".format(cid.decode('ascii'), address))
						with self.peerMutex:
							self.peerList.append((cid, RSA.importKey(args[0])))
						

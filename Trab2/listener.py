#!/usr/bin/python3

import threading
import socket

class Listener(threading.Thread):
	def __init__(self, sckt, resources, peerList, keyPair):
		self.sckt = sckt
		self.resources = resources
		self.peerList = peerList
		self.keyPair = keyPair
		super().__init__()

	def run(self):
		while True:
			data, address = self.sckt.recvfrom(1024)
			if data.startswith(b'JOIN:'):
				if data[5:] != self.keyPair['public'].exportKey():
					print("New Peer {}, adding to peer list".format(address))
					peerList.append((address, data[5:]))
				else:
					print("Received join from self, ignoring")

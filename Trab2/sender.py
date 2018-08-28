#!/usr/bin/python3

import threading
import socket


class Sender(threading.Thread):
	def __init__(self, sckt, multicastGroup, resources, peerList, keyPair):
		self.sckt = sckt
		self.multicastGroup = multicastGroup
		self.resources = resources
		self.peerList = peerList
		self.keyPair = keyPair
		super().__init__()

	def run(self):
		self.sckt.sendto(b'JOIN:' + self.keyPair['public'].exportKey(), self.multicastGroup)
		while True:
			message = str.encode(input())
			self.sckt.sendto(message, self.multicastGroup)
			

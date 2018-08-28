#!/usr/bin/python3

import threading
import socket


class Sender(threading.Thread):
	def __init__(self, sckt, multicastGroup, resources, peerList, peerMutex, keyPair):
		self.sckt = sckt
		self.multicastGroup = multicastGroup
		self.resources = resources
		self.peerList = peerList
		self.keyPair = keyPair
		self.peerMutex = peerMutex
		super().__init__()

	def run(self):
		self.sckt.sendto(b'JOIN:' + self.keyPair['public'].exportKey(), self.multicastGroup)
		peerCount = 0
		while True:
			with self.peerMutex:
				if peerCount != len(self.peerList):
					# New peer detected, send own public key for him
					self.sckt.sendto(b'ADDLIST:' + self.keyPair['public'].exportKey(), self.multicastGroup)
					
			

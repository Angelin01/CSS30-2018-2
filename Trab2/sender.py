#!/usr/bin/python3

import threading
import socket


class Sender(threading.Thread):
	def __init__(self, sckt, multicastGroup, resources):
		self.sckt = sckt
		self.multicastGroup = multicastGroup
		self.resources = resources
		super().__init__()

	def run(self):
		while True:
			message = str.encode(input())
			self.sckt.sendto(message, self.multicastGroup)
			

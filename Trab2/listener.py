#!/usr/bin/python3

import threading
import socket

class Listener(threading.Thread):
	def __init__(self, sckt, resources):
		self.sckt = sckt
		self.resources = resources
		super().__init__()

	def run(self):
		while True:
			data, address = self.sckt.recvfrom(1024)
			print("Received: {} from {}".format(data, address))

#!/usr/bin/python3

import threading
import socket

class Sender(threading.Thread):
	def __init__(self, sckt):
		self.sckt = sckt
		super().__init__()

	def run(self):
		while True:
			message = bytes(input())
			self.sckt.send(message)

#!/usr/bin/python3

import threading
from queue import Queue


class Input(threading.Thread):
	def __init__(self, commandQueue):
		self.commandQueue = commandQueue
		super().__init__()
		
	def run(self):
		while True:
			command = input()
			if command.startswith('QUIT'):
				self.commandQueue.put(command)
				break
			self.commandQueue.put(command)

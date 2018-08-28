#!/usr/bin/python3

import threading
from queue import Queue


class Input(threading.Thread):
	def __init__(self, commandQueue):
		self.commandQueue = commandQueue
		super().__init__()

	def sendCommand(self, command):
		self.commandQueue.put(command)
		
	def run(self):
		while True:
			command = input("Write a command: \n")
			self.sendCommand(command)

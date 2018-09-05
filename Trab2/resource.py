#!/usr/bin/python3
from enum import Enum
from threading import Semaphore
from queue import PriorityQueue
from queue import Empty
from time import time


class Request(object):
	def __init__(self, timestamp, cid):
		self.timestamp = timestamp
		self.cid = cid

	def __lt__(self, other):
		return self.timestamp < other.timestamp

class Status(Enum):
	RELEASED = 0
	WANTED = 1
	HELD = 2

class Resource():
	def __init__(self, status=Status.RELEASED, answerTimeout=5):
		self.status = status
		self.wantedQueue = PriorityQueue()
		self.timestamp = None
		self.peersToWait = None
		self.gotResponse = []
		self.gotNo = False
		self.answerTimeout = answerTimeout
		self.ticksPassed = 0
		
	def want(self, peersToWait):
		self.status = Status.WANTED
		self.timestamp = int(time())
		self.peersToWait = peersToWait

	def hold(self):
		self.status = Status.HELD

	def release(self):
		if self.status != Status.HELD:
			raise ValueError("Status is not HELD, can't release")

		self.status = Status.RELEASED
		self.timestamp = None
		self.peersToWait = None
		self.wantedQueue = PriorityQueue()
		self.gotResponse = []
		self.gotNo = False
		self.ticksPassed = 0

		try:
			# If there's someone waiting in the queue, return them
			return self.wantedQueue.get(False).cid
		except Empty:
			# Else return empty
			return b''

	def addAnswer(self, cid, answer):
		if self.status != Status.WANTED:
			raise ValueError("Status is not WANTED, not expecting answers")
		if cid not in self.gotResponse:
			self.gotResponse.append(cid)
			if not answer:
				self.gotNo = True
			return True
		return False

	def remainingPeers(self):
		if self.status != Status.WANTED:
			raise ValueError("Status is not WANTED, no remaining peers")
		s = set(self.gotResponse)
		return [peer for peer in self.peersToWait if peer not in s]
		
	def answerRequest(self, cid, timestamp):
		if self.status == Status.HELD or (self.status == Status.WANTED and self.timestamp < timestamp):
			self.wantedQueue.put(Request(timestamp, cid))
			# Answer NO
			return False
			# Else answer OK
		return True

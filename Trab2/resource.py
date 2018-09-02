#!/usr/bin/python3
from enum import Enum
from threading import Semaphore
from queue import PriorityQueue
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
	def __init__(self, status=Status.RELEASED):
		self.status = status
		self.wantedQueue = PriorityQueue()
		self.timestamp = None
		self.peersToWait = None
		self.gotResponse = []
		
	def hold(self, answers, peersToWait):
		self.status = Status.WANTED
		self.timestamp = int(time())
		self.peersToWait = peersToWait
		
	def release(self):
		if self.status != Status.HELD:
			return False

		self.status = Status.RELEASED
		self.timestamp = None
		self.peersToWait = None
		self.wantedQueue = PriorityQueue()
		self.gotResponse = []

	def addAnswer(self, cid, answer):
		if self.status != Status.WANTED:
			raise ValueError("Status is not WANTED, not expecting answers")
		if cid not in self.gotResponse:
			self.gotResponse.append(cid)
#			if not answer:
# @todo understand what I'm supposed to do here
			return True
		return False

	def remainingPeers(self):
		if self.status != Status.WANTED:
			raise ValueError("Status is not WANTED, no remaining peers")
		s = set(self.gotResponse)
		return [peer for peer in self.peersToWait if peer not in s]
		
	def answerRequest(self, cid, timestamp):
		pass
# @todo understand what I'm supposed to do here
#		if self.status == Status.HELD or (self.status == Status.WANTED and self.timestamp < timestamp):
#			self.wantedQueue.put(Request(timestamp, cid))
#			# Answer NO
#			return False, self.timestamp
#		else:
#			# Answer OK
#			return True, None

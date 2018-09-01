#!/usr/bin/python3
from enum import Enum
from threading import Semaphore
from queue import PriorityQueue

class Request(object):
	def __init__(self, timestamp, cid):
		self.timestamp = timestamp
		self.cid = cid
		return
	def __lt__(self, other):
		selfPriority = (self.timestamp, self.cid)
		otherPriority = (other.timestamp, other.cid)
		return selfPriority < otherPriority

class Status(Enum):
	RELEASED = 0
	WANTED = 1
	HELD = 2

class Resource():
	def __init__(self, status=Status.RELEASED):
		self.status = status
		self.wantedQueue = PriorityQueue()
		return
		
	def hold(self, answers):
		self.status = Status.WANTED
		# Send the request via multicast
		# Wait for all online peers to answer
		self.status = Status.HELD
		return
		
	def release(self):
		self.status = Status.RELEASED
		return
		
	def requested(self, cid, timestamp):
		if self.status == Status.HELD or (self.status == Status.WANTED and self.timestamp < timestamp):
			self.wantedQueue.put(Request(timestamp, cid))
			# Answer NO
			return(False)
		else:
			# Answer OK
			return(True)

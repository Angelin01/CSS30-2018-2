#!/usr/bin/python3
from enum import Enum
from threading import Semaphore


class Status(Enum):
	RELEASED = 0
	WANTED = 1
	HELD = 2


class Resource():
    def __init__(self, status=Status.RELEASED):
    	self.status = status


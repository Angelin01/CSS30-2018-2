#!/usr/bin/python3

import threading
import socket
import queue
from time import time
from time import sleep
from resource import Status
from Crypto.PublicKey import RSA
from Crypto.Signature import pkcs1_15
from Crypto.Hash import SHA256

class Sender(threading.Thread):
	def __init__(self, uid, sckt, multicastGroup, resources, peerList, keyPair, commandQueue):
		self.uid = uid.encode('ascii')
		self.sckt = sckt
		self.multicastGroup = multicastGroup
		self.resources = resources
		self.peerList = peerList
		self.keyPair = keyPair
		self.commandQueue = commandQueue
		self.signer = pkcs1_15.PKCS115_SigScheme(self.keyPair['private'])
		self.shouldRun = True
		super().__init__()
		
	def sendMessage(self, message):
		self.sckt.sendto(self.uid + b',' + self.signer.sign(SHA256.new(message)) + message, self.multicastGroup)

	def run(self):
		self.sckt.sendto(self.uid + b',JOIN,' + self.keyPair['public'].exportKey(), self.multicastGroup)
		peerCount = 0
		while self.shouldRun or not self.commandQueue.empty():
			# Check new commands
			try: 
				cmd = self.commandQueue.get(block=False).strip('\n')
				# Execute the command
				if cmd.startswith("WANT"):
					rid = cmd[4:]
					if not rid.isdigit() or int(rid) >= len(self.resources):
						print("Error: requested resource {} does not exist.".format(rid))
					elif self.resources[int(rid)].status == Status.HELD:
						print("Error: I already hold that resource, release it first before asking again")
					else:
						self.resources[int(rid)].want([cid for cid, __ in self.peerList])
						self.sendMessage(b'WANT,' + rid.encode('ascii') + b',' + str(int(time())).encode('ascii'))

				elif cmd.startswith("RELEASE"):
					rid = cmd[7:]
					if not rid.isdigit() or int(rid) >= len(self.resources):
						print("Error: requested resource {} does not exist.".format(rid))
					elif self.resources[int(rid)].status == Status.HELD:
						nextPeer = self.resources[int(rid)].release()
						self.sendMessage(b'RELEASE,' + rid.encode('ascii') + b',' + nextPeer)
					else:
						print("Error: I don't hold resource {}, so I can't release it".format(rid))
						
				elif cmd.startswith("STATUS"):
					rid = cmd[6:]
					if not rid.isdigit() or int(rid) >= len(self.resources):
						print("Error: requested resource {} does not exist.".format(rid))
					else:
						print("Resource {} status: {}".format(rid,  self.resources[int(rid)].status))
					
				# Answer OK to WANT
				elif cmd.startswith("OK"):
					rid = cmd.split(',')[1]
					if not rid.isdigit() or int(rid) >= len(self.resources):
						print("Error: resource requested does not exist.")
					else:
						print("Sending OK")
						self.sendMessage(b'ANSWER,' + rid.encode('ascii') + b',' + b'OK')

				# Answer NO to WANT
				elif cmd.startswith("NO"):
					rid = cmd.split(',')[1]
					if not rid.isdigit() or int(rid) >= len(self.resources):
						print("Error: resource requested does not exist.")
					else:
						print("Sending NO")
						self.sendMessage(b'ANSWER,' + rid.encode('ascii') + b',' + b'NO')
				
				# Send stuff to new peers
				elif cmd.startswith("ADDLIST"):
					self.sckt.sendto(self.uid + b',ADDLIST,' + self.keyPair['public'].exportKey(), self.multicastGroup)
					
				elif cmd.startswith("QUIT"):
					sleep(0.2)  # Wait at least one cycle as to not interrupt an incoming/outgoing message
					self.shouldRun = False
					
			except queue.Empty:
				pass
			
		# Left loop, leaving
		self.sendMessage(b"LEAVE")
					
	def stop(self):
		self.shouldRun = False

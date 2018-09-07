#!/usr/bin/python3

import threading
import socket
from resource import Status
from Crypto.PublicKey import RSA
from Crypto.Signature import pkcs1_15
from Crypto.Hash import SHA256

class Listener(threading.Thread):
	def __init__(self, uid, sckt, resources, peerList, peerMutex, keyPair, commandQueue):
		self.uid = uid.encode('ascii')
		self.sckt = sckt
		self.resources = resources
		self.peerList = peerList
		self.keyPair = keyPair
		self.peerMutex = peerMutex
		self.shouldRun = True
		self.commandQueue = commandQueue
		super().__init__()
		
	def verifyMessage(self, peerVerifier, message, signature):
		try:
			peerVerifier.verify(SHA256.new(message), signature)
		except ValueError:
			return False
		return True

	def run(self):
		while self.shouldRun:
			try:
				data, address = self.sckt.recvfrom(1024)
				cid, toVerify = data.split(b',', 1)
				
				if cid != self.uid: # Check if it isn't myself
					# Identify the peer in the peerList first
					peer = None
					for p in self.peerList:
						if p[0] == cid:
							peer = p
							
					# If I could not identify the peer, ignore
					if peer == None:
						raise UnboundLocalError('Unidentified peer. Someone is doing something fishy')

					# Do not check signature in JOINS and ADDLISTS
					if toVerify.startswith(b'JOIN') or toVerify.startswith(b'ADDLIST'):
						cmd, *args = toVerify.split(b',')
					elif not self.verifyMessage(peer[1], toVerify[256:], toVerify[:256]):
						print("Could not verify command from {}, someone is doing something fishy".format(cid.decode('ascii')))
						continue
					else:
						cmd, *args = toVerify[256:].split(b',')

					# Check if the message is a new join
					if cmd == b'JOIN':
					# Join commands have the following structure:
					# [CID],JOIN,[PUBLIC_KEY]
						print("New Peer '{}'{}, adding to peer list".format(cid.decode('ascii'), address))
						with self.peerMutex:
							self.peerList.append((cid, pkcs1_15.PKCS115_SigScheme(RSA.importKey(args[0]))))
			
					# Check for messages from existing peers
					elif cmd == b'ADDLIST':
					# Addlist commands have the following structure:
					# [CID],ADDLIST,[PUBLIC_KEY]
						# Check if I don't already have the peer
						if cid not in [x for x,__ in self.peerList]:
							print("New existing Peer '{}'{}, adding to peer list".format(cid.decode('ascii'), address))
							with self.peerMutex:
								self.peerList.append((cid, pkcs1_15.PKCS115_SigScheme(RSA.importKey(args[0]))))
				
					# Check for leavers
					elif cmd == b'LEAVE':
					# Leave commands have the following structure:
					# [CID],[SIGNATURE]LEAVE
						print("Peer '{}' leaving, removing from peer list".format(cid.decode('ascii')))
						for peer in self.peerList:
							if peer[0] == cid:
								self.peerList.remove(peer)

					# Check for resource messages
					elif cmd == b'WANT':
					# Want commands have the following structure:
					# [CID],[SIGNATURE]WANT,[RESOURCE_ID],[TIMESTAMP]
						print("Peer '{}' requested resource {}".format(cid.decode('ascii'), int(args[0])))
						if self.resources[int(args[0])].answerRequest(cid, int(args[1])):
							self.commandQueue.put('OK,' + args[0].decode('ascii'))
						else:
							self.commandQueue.put('NO,' + args[0].decode('ascii'))

					elif cmd == b'RELEASE':
					# @todo Get resource if it's my turn else just remove the guy from the queue if I want it
					# Release commands have the following structure:
					# [CID],[SIGNATURE]RELEASE,[RESOURCE_ID],[NEXT_CID OR EMPTY]
						pass

					elif cmd == b'ANSWER':
					# Answer commands have the following structure:
					# [CID],[SIGNATURE]ANSWER,[RESOURCE_ID],[OK OR NO]
						# Check if I want the resource, if so add the response
						if self.resources[int(args[0])].status == Status.WANTED:
							print("Peer '{}' answered '{}' for resource {}".format(cid.decode('ascii'), args[1].decode('ascii'), int(args[0])))
							# Put False as the strict check with "NO", everything else is True
							self.resources[int(args[0])].addAnswer(cid, False if args[1] == b'NO' else True)
			except socket.timeout:
				pass
			except UnboundLocalError:
				pass
			
			# Checking each resource if I got answers
			for resource in self.resources:
				if resource.status == Status.WANTED:
					resource.ticksPassed += 1
					if resource.ticksPassed > resource.answerTimeout and len(self.peerList) > len(resource.gotResponse):
						# Remove from peer list everyone that didn't answer
						remainingPeers = resource.remainingPeers()
						for peer in self.peerList:
							if peer[0] in remainingPeers:
								self.peerList.remove(peer)
					# Check if I got all responses and there was no negative
					if len(self.peerList) == len(resource.gotResponse) and resource.gotNo == False:
						print("Got all answers positive, holding")
						resource.hold()
					# If I got a no, have to wait for my release

		# Out of while loop, release and quit
		for i in range(len(self.resources)):
			if self.resources[i].status == Status.HELD:
				self.resources[i].release()
				self.commandQueue.put("RELEASE,{}".format(i))
							
	def stop(self):
		self.shouldRun = False

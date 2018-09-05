#!/usr/bin/python3

# Standard or external libraries
import sys
import threading
import socket
import argparse
from struct import pack
from Crypto.PublicKey import RSA
# Internal stuffs
from listener import Listener
from sender import Sender
from resource import Resource
from input import Input
from queue import Queue

def main(address, port, privateKey, publicKey, name):
	# Importing assimetric keys
	priv = RSA.importKey(privateKey.read())
	pub = RSA.importKey(publicKey.read())
	
	sockterino = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
	sockterino.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
	sockterino.bind(('', port))
	sockterino.settimeout(0.2) # Not the same as settimeout(None)!!!

	# Create a queue for sharing data between threads
	commandQueue = Queue()
	
	# Join the multicast group
	sockterino.setsockopt(socket.IPPROTO_IP, 
	                      socket.IP_ADD_MEMBERSHIP, 
	                      pack('4sL', socket.inet_aton(address), socket.INADDR_ANY))                
	                      
	# Setting Time To Live to 1 so the packets don't leave the local network
	sockterino.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, pack('b', 1))

	# Instantiating resources e threads
	peerMutex = threading.Semaphore()
	peerList = []
	resources = [Resource(), Resource()]
	threadListener = Listener(name, sockterino, resources, peerList, peerMutex, {'private':priv, 'public':pub}, commandQueue)
	threadSender = Sender(name, sockterino, (address, port), resources, peerList,  peerMutex, {'private':priv, 'public':pub}, commandQueue)
	threadInput = Input(commandQueue)

	# Starting and joining threads
	threadListener.start()
	threadSender.start()
	threadInput.start()
	threadInput.join()

	# Input asked to quit
	threadListener.stop()
	threadListener.join()
	threadSender.stop()
	threadSender.join()

	#sys.exit(0)
	

if __name__ == "__main__":
	parser = argparse.ArgumentParser(description='Multicast peer client for controlling 2 resources')
	parser.add_argument('-a', '--address', action='store', help='Multicast address to connect to', required=True)
	parser.add_argument('-p', '--port', action='store', type=int, help='Multicast port to connect to', required=True)
	parser.add_argument('-i', '--privateKey', type=argparse.FileType('r'), help='Private key file. See ssh-keygen', required=True)
	parser.add_argument('-u', '--publicKey', type=argparse.FileType('r'), help='Public key file. See ssh-keygen', required=True)
	parser.add_argument('-n', '--name', action='store', help='Unique identifier number. Must be an int.', required=True)

	args = parser.parse_args()
	
	main(args.address, args.port, args.privateKey, args.publicKey, args.name)
	

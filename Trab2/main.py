#!/usr/bin/python3

import threading
import socket
from struct import pack
from listener import Listener
from sender import Sender

def main(address, port):
	sockterino = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
	sockterino.bind(('', port))
	
	sockterino.setsockopt(socket.IPPROTO_IP, 
	                      socket.IP_ADD_MEMBERSHIP, 
						  pack('4sL', socket.inet_aton(address), socket.INADDR_ANY))
	sockterino.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, pack('b', 1))

	threadListener = Listener(sockterino)
	threadSender = Sender(sockterino, (address, port))

	threadListener.start()
	threadSender.start()
	threadListener.join()
	threadSender.join()

if __name__ == "__main__":
	ADDRESS = '224.3.29.71'
	PORT = 9999
	main(ADDRESS, PORT)

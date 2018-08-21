#!/usr/bin/python3

import threading
import socket
from listener import Listener
from sender import Sender

def main(address, port):
	sockterino = socket.socket(type=socket.SOCK_DGRAM)
	sockterino.bind((address, port))

	threadListener = Listener(sockterino)
	threadSender = Sender(sockterino)

	threadListener.start()
	threadSender.start()
	threadListener.join()
	threadSender.join()

if __name__ == "__main__":
	ADDRESS = '232.232.232.232'
	PORT = 9999
	main(ADDRESS, PORT)

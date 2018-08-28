#!/usr/bin/python3

import sys
import threading
import socket
from struct import pack
from listener import Listener
from sender import Sender
from resource import Resource


def main(address, port):
	sockterino = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
	sockterino.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
	sockterino.bind(('', port))
	
	# Dar join no grupo multicast
	sockterino.setsockopt(socket.IPPROTO_IP, 
	                      socket.IP_ADD_MEMBERSHIP, 
	                      pack('4sL', socket.inet_aton(address), socket.INADDR_ANY))                
	                      
	# Settando Time To Live para 1 para nao sair da rede local
	sockterino.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, pack('b', 1))

	# Instanciando recursos e threads
	resources = [Resource(), Resource(8)]
	threadListener = Listener(sockterino, resources)
	threadSender = Sender(sockterino, (address, port), resources)

	# Iniciando e aguardando threads
	threadListener.start()
	threadSender.start()
	threadListener.join()
	threadSender.join()
	

if __name__ == "__main__":
	main(sys.argv[1], int(sys.argv[2]))

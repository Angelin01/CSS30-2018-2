#!/usr/bin/python3
# Bibliotecas externas ou padrao
import sys
import threading
import socket
import argparse
from struct import pack
from Crypto.PublicKey import RSA
# Interno
from listener import Listener
from sender import Sender
from resource import Resource
from input import Input
from queue import Queue

def main(address, port, privateKey, publicKey, name):
	# Importing assimetrics keys
	priv = RSA.importKey(privateKey.read())
	pub = RSA.importKey(publicKey.read())
	
	sockterino = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
	sockterino.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
	sockterino.bind(('', port))

	# Create a queue for sharing data between threads
	commandQueue = Queue()
	
	# Dar join no grupo multicast
	sockterino.setsockopt(socket.IPPROTO_IP, 
	                      socket.IP_ADD_MEMBERSHIP, 
	                      pack('4sL', socket.inet_aton(address), socket.INADDR_ANY))                
	                      
	# Settando Time To Live para 1 para nao sair da rede local
	sockterino.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, pack('b', 1))

	# Instanciando recursos e threads
	peerMutex = threading.Semaphore()
	peerList = []
	resources = [Resource(), Resource()]
	threadListener = Listener(name, sockterino, resources, peerList, peerMutex, {'private':priv, 'public':pub})
	threadSender = Sender(name, sockterino, (address, port), resources, peerList,  peerMutex, {'private':priv, 'public':pub}, commandQueue)
	threadInput = Input(commandQueue)

	# Iniciando e aguardando threads
	threadListener.start()
	threadSender.start()
	threadInput.start()
	threadListener.join()
	threadSender.join()
	threadInput.join()
	

if __name__ == "__main__":
	parser = argparse.ArgumentParser(description='Multicast peer client for controlling 2 resources')
	parser.add_argument('-a', '--address', action='store', help='Multicast address to connect to', required=True)
	parser.add_argument('-p', '--port', action='store', type=int, help='Multicast port to connect to', required=True)
	parser.add_argument('-i', '--privateKey', type=argparse.FileType('r'), help='Private key file. See ssh-keygen', required=True)
	parser.add_argument('-u', '--publicKey', type=argparse.FileType('r'), help='Public key file. See ssh-keygen', required=True)
	parser.add_argument('-n', '--name', action='store', help='Unique identifier number. Must be an int.', required=True)

	args = parser.parse_args()
	
	main(args.address, args.port, args.privateKey, args.publicKey, args.name)
	

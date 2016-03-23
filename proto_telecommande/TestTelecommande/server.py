import socket
from telecommande import *
from rgbmatrix import Adafruit_RGBmatrix

socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
socket.bind(('', 8192)) #couple (hostname,port)
matrix = Adafruit_RGBmatrix(32,2) 

while True:
        socket.listen(0) #Nombre de connexions en attente
        client, address = socket.accept()
        print "{} connected".format( address )
        client.send('Bonjour\n')	    
        
	try:
            thread_telecommande = Telecommande()
            thread_telecommande.setDaemon(True)
            thread_telecommande.start()
            thread_telecommande.matrix = matrix
            response = client.recv(255) #Taille buffer
            client.send(response)
            response = response.replace('\n', '')
            while response != 'QUIT' and response != '':
                thread_telecommande.setMessage(response)
                response = client.recv(255) #Taille buffer
                client.send(response)
                response = response.replace('\n', '')
            thread_telecommande.setMessage('QUIT')
            print "Quit received"
        except Exception, e:
            print 'EXCEPTION : '+str(e)
            thread_telecommande.setMessage('QUIT')

print "Close"
client.close()
socket.close()

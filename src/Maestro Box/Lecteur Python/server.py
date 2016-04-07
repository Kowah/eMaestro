import socket
import os
from telecommande import *
from rgbmatrix import Adafruit_RGBmatrix

socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
socket.bind(('', 8192)) #couple (hostname,port)
matrix = Adafruit_RGBmatrix(32,2) 
logo = Image.open("logo.png")
logo.load()
matrix.SetImage(logo.im.id,0,0)

while True:
        socket.listen(0) #Nombre de connexions en attente
        
        client, address = socket.accept()
        print "{} connected".format( address )
	try:
            thread_telecommande = Telecommande()
            thread_telecommande.setDaemon(True)
            thread_telecommande.start()
            thread_telecommande.matrix = matrix
            thread_telecommande.logo = logo
            message = client.recv(255) #Taille buffer
            message = message.replace('\n', '')
	    thread_telecommande.name = message
	    message = client.recv(255)
	    message = message.replace('\n','')
            while message != 'QUIT' and message != 'SHUTDOWN' and message != '':
                thread_telecommande.setMessage(message)
                message = client.recv(255) #Taille buffer
                message = message.replace('\n', '')
            thread_telecommande.setMessage('QUIT')
	    if message == 'SHUTDOWN':
		print "Shutdown"
	        os.system("shutdown now")
            print "Quit received"
        except Exception, e:
            print 'EXCEPTION : '+str(e)
            thread_telecommande.setMessage('QUIT')

print "Close"
client.close()
socket.close()

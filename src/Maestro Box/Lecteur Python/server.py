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
            thread_telecommande.matrix = matrix
            thread_telecommande.logo = logo
	    quit = 0
	    thread_telecommande.start()
	    while quit == 0:
             message = client.recv(255) #Taille buffer
             message = message.replace('\n', '')
             donnees = message.split(",")
	     thread_telecommande.name = donnees[0]
	     thread_telecommande.debut = donnees[1]
	     thread_telecommande.fin = donnees[2]
	     message = client.recv(255)
	     message = message.replace('\n','')
             while message != 'QUIT' and message != 'PAUSE' and  message != 'SHUTDOWN' and message != '' and thread_telecommande.display.get_etat != 'stop':
                  message = client.recv(255) #Taille buffer
                  message = message.replace('\n', '')
                  thread_telecommande.setMessage(message)
             if message == "QUIT"
			     thread_telecommande.setMessage('QUIT')
	     thread_telecommande.display.set_etat('stop')
	     if message == 'SHUTDOWN':
		 print "Shutdown"
		 thread_telecommande.setMessage('QUIT')
	         os.system("shutdown now")
             print "Quit received"
        except Exception, e:
            print 'EXCEPTION : '+str(e)
            thread_telecommande.setMessage('QUIT')

print "Close"
client.close()
socket.close()

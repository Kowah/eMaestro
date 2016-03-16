import socket
from telecommande import *

socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
socket.bind(('', 8192)) #couple (hostname,port)

while True:
        socket.listen(0) #Nombre de connexions en attente
        client, address = socket.accept()
        print "{} connected".format( address )
        client.send('Bonjour\n')
        
        thread_telecommande = Telecommande()
	thread_telecommande.setDaemon(True)
        thread_telecommande.start()
        
        try:
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
	    thread_telecommande.exit()
        except Exception, e:
            print 'EXCEPTION : '+str(e)
            thread_telecommande.setMessage('QUIT')

print "Close"
client.close()
socket.close()

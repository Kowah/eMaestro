import socket
from telecommande import *

socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
socket.bind(('', 15555)) #couple (hostname,port)

quitBool = False

while not quitBool:
        socket.listen(0) #Nombre de connexions en attente
        client, address = socket.accept()
        print "{} connected".format( address )
        
        thread_telecommande = Telecommande()
        thread_telecommande.start()
        
        try:
            response = client.recv(255) #Taille buffer
            response = response.replace('\n', '')
            while response != 'QUIT' and response != '':
                thread_telecommande.setMessage(response)
                response = client.recv(255) #Taille buffer
                response = response.replace('\n', '')
            thread_telecommande.setMessage('QUIT')
            quitBool = True
        except Exception, e:
            print 'EXCEPTION : '+str(e)
            thread_telecommande.setMessage('QUIT')
            quitBool = True

print "Close"
client.close()
socket.close()
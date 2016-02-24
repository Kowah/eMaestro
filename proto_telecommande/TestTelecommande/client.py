import socket
import time


hote = "localhost"
port = 15555

socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
socket.connect((hote, port))
print "Connection on {}".format(port)

socket.send('PLAY')
time.sleep(2)
socket.send('PAUSE')


time.sleep(1)
socket.send('QUIT')
print "Close"
socket.close()
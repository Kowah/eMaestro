import threading
import time
import Image
import ImageDraw
from rgbmatrix import Adafruit_RGBmatrix
from chargeur_partition import *
from afficheur import *

class Telecommande(threading.Thread):

    def __init__(self):
        threading.Thread.__init__(self)
        self.message=''
        self.logo=''
        self.matrix = None
        self.count = 1
        
    def setMessage(self,msg):
        self.message=msg
        
    def run(self):
	while self.message != 'QUIT':
		if self.message == 'PLAY':
			self.play()
		elif self.message == 'PAUSE':
			self.pause()
	self.quit()

    def play(self):
       cp = Chargeur_partition(27)
        while self.message == 'PLAY':
            afficher(cp.get_liste_de_lecture(), cp.get_images_ephemeres(),self.matrix)
    
    def pause(self):
        print "PAUSE"
        self.setMessage('')
             
    def quit(self):
	print "End"
	self.matrix.Clear()
	self.matrix.SetImage(self.logo.im.id,0,0)

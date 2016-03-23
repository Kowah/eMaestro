import threading
import time
import Image
import ImageDraw
from chargeur_partition import *
from afficheur import *




from rgbmatrix import Adafruit_RGBmatrix

class Telecommande(threading.Thread):

    def __init__(self):
        threading.Thread.__init__(self)
        self.message=''
	self.matrix = None
	count = 1
        
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
	cp = Chargeur_partition(6)
        while self.message == 'PLAY':
            afficher(cp.get_liste_de_lecture(), cp.get_images_ephemeres(),self.matrix)
    
    def pause(self):
        print "PAUSE"
        self.setMessage('')
             
    def quit(self):
	print "End"
	self.matrix.Clear()     

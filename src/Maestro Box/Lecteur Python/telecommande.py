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
	self.name = 0
        
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
       print "playing "+ self.name
       cp = Chargeur_partition(ord(self.name))
       map_mesure_modif = cp.get_map_mesures_modif()
       display = afficheur(self.matrix)
       while self.message == 'PLAY':
            display.lire(map_mesure_modif,1,1,map_mesure_modif["mesure_fin_musique"],1)
    
    def pause(self):
        print "PAUSE"
        self.setMessage('')
             
    def quit(self):
	print "End"
	self.matrix.Clear()
	self.matrix.SetImage(self.logo.im.id,0,0)

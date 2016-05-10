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
	self.debut = 0
	self.fin = 0
	self.display = None
        
    def setMessage(self,msg):
        self.message=msg
        
    def run(self):
	self.display = Afficheur(self.matrix)
	while self.message != 'QUIT':
	  if self.message == 'PLAY':
	    print(self.debut)
	    print(self.fin)
	    self.play()
	    self.matrix.Clear()
	    self.matrix.SetImage(self.logo.im.id,0,0)
          elif self.message == 'PAUSE':
	    self.pause()
	self.quit()

    def play(self):
       print "playing "+ self.name
       cp = Chargeur_partition(self.name)
       map_mesure_modif = cp.get_map_mesures_modif()
       self.display.lire(map_mesure_modif,int(self.debut),1,int(self.fin),1)
    
    def pause(self):
        print "PAUSE"
        
    def quit(self):
	print "End"
	self.matrix.Clear()
	self.matrix.SetImage(self.logo.im.id,0,0)

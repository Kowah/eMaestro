import threading
import time
import Image
import ImageDraw
from rgbmatrix import Adafruit_RGBmatrix

class Telecommande(threading.Thread):

    def __init__(self):
        threading.Thread.__init__(self)
        self.message=''
	self.matrix = None
        
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
        count = 1
        while self.message == 'PLAY':
            filename = '4_'+str(count)+'.png'
	    self.matrix.Clear()
            image = Image.open(filename)
	    image.load()
	    self.matrix.SetImage(image.im.id,0,0)
	    print filename
            count = (count % 4)+1
            time.sleep(1)
    
    def pause(self):
        print "PAUSE"
        self.setMessage('')
             
    def quit(self):
	print "End"
	self.matrix.Clear()     

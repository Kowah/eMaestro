from threading import Thread
import time

class Telecommande(Thread):
    
    
    def __init__(self):
        Thread.__init__(self)
        self.message=''
        
    def setMessage(self,msg):
        self.message=msg
        
    def run(self):
        while self.message != 'QUIT':
            
            if self.message == 'PLAY':
                self.play()
            elif self.message == 'PAUSE':
                self.pause()
    
    def play(self):
        count = 1
        while self.message == 'PLAY':
            filename = '4_'+str(count)+'.png'
            print filename
            count = (count % 4)+1
            time.sleep(1)
    
    def pause(self):
        print 'black.png'
        self.setMessage('')
        
    def traiterMessage(self,msg):
        self.setMessage(msg)
        print 'Telecommande recoit '+msg
        switch = {
                  "PLAY":self.play,
                  "PAUSE":self.pause
                  }
        fun = switch.get(msg, lambda: None)
        fun()
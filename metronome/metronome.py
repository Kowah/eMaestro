import Image
import ImageDraw
import time
from rgbmatrix import Adafruit_RGBmatrix

matrix = Adafruit_RGBmatrix(32, 2)

logo = Image.open("logo.png")
logo.load()
image1 = Image.open("4_1.png")
image1.load()
image2 = Image.open("4_2.png")
image2.load()
image3 = Image.open("4_3.png")
image3.load()
image4 = Image.open("4_4.png")
image4.load()

def metronome(t):
  matrix.SetImage(logo.im.id, 0, 0)
  time.sleep(t * 4)
  for i in range(20):
    matrix.SetImage(image1.im.id, 0, 0)
    time.sleep(t)
    matrix.SetImage(image2.im.id, 0, 0)
    time.sleep(t)
    matrix.SetImage(image3.im.id, 0, 0)
    time.sleep(t)
    matrix.SetImage(image4.im.id, 0, 0)
    time.sleep(t)
  

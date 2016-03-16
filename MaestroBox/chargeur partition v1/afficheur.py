import Image
import ImageDraw
import time
from rgbmatrix import Adafruit_RGBmatrix

matrix = Adafruit_RGBmatrix(32, 2)

def afficher(liste_de_lecture, images_ephemeres):
# a mettre dans chargeur partition en tete de liste de lecture le decompte de la premiere mesure en partant du temps par mesure jusqu'a 1 en fonction du tempo, + logo?
  for (image, tempo) in liste_de_lecture:
    matrix.SetImage(images_ephemeres[image].im.id, 0, 0)
    time.sleep(tempo)


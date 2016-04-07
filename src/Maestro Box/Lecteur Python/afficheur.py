import Image
import ImageDraw
import time

def afficher(liste_de_lecture, images_ephemeres,matrix):
# a mettre dans chargeur partition en tete de liste de lecture le decompte de la premiere mesure en partant du temps par mesure jusqu'a 1 en fonction du tempo, + logo?
	for (image, tempo) in liste_de_lecture:
   	    images_ephemeres[image].load()
	    matrix.SetImage(images_ephemeres[image].im.id, 0, 0)
	    time.sleep(tempo/100)


from chargeur_partition import *
from afficheur import *

# mettre les bonnes infos de la bdd dans lecteur_bdd.py

cp = Chargeur_partition(2)
afficher(cp.get_liste_de_lecture(), cp.get_images_ephemeres())

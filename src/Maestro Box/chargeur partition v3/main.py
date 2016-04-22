from chargeur_partition import *
from afficheur import *

chargeur = Chargeur_partition(1)
map_mesure_modif = chargeur.get_map_mesure_modif()

lire(map_mesure_modif,1,1,map_mesure_modif["mesure_fin_musique"],1)

# a + 4 * 60/float(125L) * 16 + 7 * 60 /float(50L) * 8 + 4 * 60 /float(50L) * 7 + 5 * 60 /float(33L) * 12 + 4 * 60 /float(30L) * 15

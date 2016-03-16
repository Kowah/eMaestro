from lecteur_bdd import *
import Image

path_images = "images/cercles/"
host="localhost"
user="tristan"
pwd="1234"
db="partition_emaestro_db"

class Chargeur_partition:


"""    def __init__(self):
    self.id_partition_chargee = None
    self.images_ephemeres = {}
    self.liste_de_lecture = []
    self.map_mesures_temps = {"nb_temps" : 0}
    self.lecteur_bdd = LecteurBDD(host, user, pwd, db)
"""

  def __init__(self, id_musique):
    self.id_partition_chargee = id_musique
    self.images_ephemeres = {}
    self.liste_de_lecture = []
    self.map_mesures_temps = {"nb_temps" : 0}
    self.lecteur_bdd = LecteurBDD(host, user, pwd, db)
    self.charger_partition(id_musique)


  def creer_images_ephemeres(self, temps_par_mesure):
    for t in range(1,temps_par_mesure + 1):
      self.images_ephemeres[str(temps_par_mesure)+'.'+str(t)] = Image.open(path_images+str(temps_par_mesure)+'_'+str(t)+'.png')


  def creer_liste_lecture(self, mesure_debut, mesure_fin, temps_par_mesure, tempo):
    next_nb_temps = self.map_mesures_temps["nb_temps"]
    for m in range(mesure_debut, mesure_fin):
      self.map_mesures_temps[m] = next_nb_temps
      for t in range(1,temps_par_mesure + 1):
        self.liste_de_lecture = self.liste_de_lecture + [(str(temps_par_mesure)+'.'+str(t), tempo)]
        next_nb_temps += 1
    self.map_mesures_temps["nb_temps"] = next_nb_temps


  def charger_partition(self, id_musique):
      selection = self.lecteur_bdd.getInfosMusique(id_musique)
      mesure_fin = self.lecteur_bdd.getMesureFin(id_musique)
      for i in range(len(selection)):
        (mesure_debut, temps_par_mesure, tempo) = selection[i]
        if (str(temps_par_mesure)+'.1') not in self.images_ephemeres:
          self.creer_images_ephemeres(temps_par_mesure)
        if (i+1 < len(selection)):
          self.creer_liste_lecture(mesure_debut, selection[i+1][0], temps_par_mesure, tempo)
        else:
          self.creer_liste_lecture(mesure_debut, (mesure_fin + 1), temps_par_mesure, tempo)


  def get_id_partition_chargee(self):
    return self.id_partition_chargee


  def get_images_ephemeres(self):
     return self.images_ephemeres


  def get_liste_de_lecture(self):
    return self.liste_de_lecture


  def get_map_mesures_temps(self):
    return self.map_mesures_temps





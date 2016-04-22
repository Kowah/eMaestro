from lecteur_bdd import *

host="localhost"
user="guest"
pwd="guest"
db="emaestro"

#host="localhost"
#user="tristan"
#pwd="1234"
#db="emaestro"

class Chargeur_partition:


  def __init__(self, id_musique):
    self.id_partition_chargee = id_musique
    self.map_mesures_modif = {}
    self.lecteur_bdd = LecteurBDD(host, user, pwd, db)
    self.charger_partition(id_musique)


  def charger_partition(self, id_musique):

      self.map_mesures_modif = self.lecteur_bdd.getDicoVariationTemps(id_musique)
      self.map_mesures_modif["mesure_fin_musique"] = self.lecteur_bdd.getMesureFin(id_musique)

#  def com():
      dicotmp = self.lecteur_bdd.getDicoVariationIntensite(id_musique)
      for m in dicotmp:
        if m in self.map_mesures_modif:
          self.map_mesures_modif[m].update(dicotmp[m])
        else:
          self.map_mesures_modif[m] = dicotmp[m]

      dicotmp = self.lecteur_bdd.getDicoPartie(id_musique)
      for m in dicotmp:
        if m in self.map_mesures_modif:
          self.map_mesures_modif[m].update(dicotmp[m])
        else:
          self.map_mesures_modif[m] = dicotmp[m]

      dicotmp = self.lecteur_bdd.getDicoMesuresNonLues(id_musique)
      for m in dicotmp:
        if m in self.map_mesures_modif:
          self.map_mesures_modif[m].update(dicotmp[m])
        else:
          self.map_mesures_modif[m] = dicotmp[m]

      dicotmp = self.lecteur_bdd.getDicoReprise(id_musique)
      for m in dicotmp:
        if m in self.map_mesures_modif:
          self.map_mesures_modif[m].update(dicotmp[m])
        else:
          self.map_mesures_modif[m] = dicotmp[m]

      dicotmp = self.lecteur_bdd.getDicoAlerte(id_musique)
      for m in dicotmp:
        if m in self.map_mesures_modif:
          self.map_mesures_modif[m].update(dicotmp[m])
        else:
          self.map_mesures_modif[m] = dicotmp[m]

      dicotmp = self.lecteur_bdd.getDicoVariationRythme(id_musique)
      for m in dicotmp:
        if m in self.map_mesures_modif:
          self.map_mesures_modif[m].update(dicotmp[m])
        else:
          self.map_mesures_modif[m] = dicotmp[m]

      dicotmp = self.lecteur_bdd.getDicoSuspension(id_musique)
      for m in dicotmp:
        if m in self.map_mesures_modif:
          self.map_mesures_modif[m].update(dicotmp[m])
        else:
          self.map_mesures_modif[m] = dicotmp[m]

      dicotmp = self.lecteur_bdd.getDicoArmature(id_musique)
      for m in dicotmp:
        if m in self.map_mesures_modif:
          self.map_mesures_modif[m].update(dicotmp[m])
        else:
          self.map_mesures_modif[m] = dicotmp[m]


  def get_id_partition_chargee(self):
    return self.id_partition_chargee


  def get_map_mesures_modif(self):
     return self.map_mesures_modif







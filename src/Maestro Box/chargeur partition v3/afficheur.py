import Image
import sched
import time
import gc
from rgbmatrix import Adafruit_RGBmatrix

chemin_images = "drawable/"

temps_affichage_logo = 0
temps_affichage_fin = 3

pos_temps_h = (8,0)
pos_temps_g = (8, 8)
pos_temps_d = (32, 8)
pos_temps_b = (8, 24)
pos_intensite = (16, 8)
pos_passage = (0, 24)
pos_armature_symb = (56, 16)
pos_armature_chiffre = (48, 16)
pos_centaine = (40, 0)
pos_dizaine = (48, 0)
pos_unite = (56, 0)
pos_partie = (40, 8)
pos_alerte = (40, 24)

class Afficheur :

  def __init__(self,mat):
    self.matrix = mat
    self.etat = "stop"

  def lire(self, m, mesure_debut_lecture, passage_debut_lecture, mesure_fin_lecture, passage_fin_lecture):

    self.afficher_logo()

    map_mesures_modif = m.copy()
    descripteur = map_mesures_modif["1.1"]
    descripteur["mesure_courante"] = 1
    descripteur["passage_reprise_courant"] = 1
    descripteur["intensite_courante"] = -1
    descripteur["alteration_courante"] = 0
    descripteur["partie_courante"] = '-1'

    while(descripteur["mesure_courante"], descripteur["passage_reprise_courant"]) != (mesure_debut_lecture, passage_debut_lecture):
      if (str(descripteur["mesure_courante"]) + '.' + str(descripteur["passage_reprise_courant"])) in map_mesures_modif :
        descripteur = dict(descripteur, ** map_mesures_modif[str(descripteur["mesure_courante"]) + '.' + str(descripteur["passage_reprise_courant"])])
      if "mesure_non_lue" in descripteur :
        del descripteur["mesure_non_lue"]
      if "prochaine_mesure" in descripteur:
        descripteur["mesure_courante"] = descripteur["prochaine_mesure"]
        del descripteur["prochaine_mesure"]
      else:
        descripteur["mesure_courante"] += 1
      if "prochain_passage" in descripteur:
        descripteur["passage_reprise_courant"] = descripteur["prochain_passage"]
        del descripteur["prochain_passage"]

    gc.disable()

    self.etat = "play"

    scheduler = sched.scheduler(time.time, time.sleep)
    scheduler.enter(temps_affichage_logo, 1, self.wait, ())
    scheduler.run()

    self.afficher_decompte(descripteur["temps_par_mesure"], descripteur["tempo"], scheduler)

    while ((self.etat != "stop") and (descripteur["mesure_courante"], descripteur["passage_reprise_courant"]) != (mesure_fin_lecture + 1, passage_fin_lecture)):

      gc.enable()

      if(self.etat == "play"):

        if (str(descripteur["mesure_courante"]) + '.' + str(descripteur["passage_reprise_courant"])) in map_mesures_modif :
          descripteur = dict(descripteur, ** map_mesures_modif[str(descripteur["mesure_courante"]) + '.' + str(descripteur["passage_reprise_courant"])])

        if ("mesure_debut_reprise" in descripteur):
          del descripteur["mesure_debut_reprise"]
          descripteur["afficher_passage"] = True
          dico_tmp = {
"temps_par_mesure" : descripteur["temps_par_mesure"],
"tempo" : descripteur["tempo"],
"unite_pulsation" : descripteur["unite_pulsation"],
"temps_debut_intensite_1" : True,
"intensite_1" : descripteur["intensite_courante"],
"label" : descripteur["partie_courante"],
"temps_debut_armature_1" : True,
"alteration_1" : descripteur["alteration_courante"]}

          if (str(descripteur["mesure_courante"]) + '.2') in map_mesures_modif:
            map_mesures_modif[str(descripteur["mesure_courante"]) + '.2'] = dict(dico_tmp, ** map_mesures_modif[str(descripteur["mesure_courante"]) + '.2'])
          else:
            map_mesures_modif[str(descripteur["mesure_courante"]) + '.2'] = dico_tmp.copy()

          if ("nb_temps_intensite_1" in descripteur):
            map_mesures_modif[str(descripteur["mesure_courante"]) + '.2'] = dict({"nb_temps_intensite_1" : descripteur["nb_temps_intensite_1"]}, ** map_mesures_modif[str(descripteur["mesure_courante"]) + '.2'])
          else:
            map_mesures_modif[str(descripteur["mesure_courante"]) + '.2'] = dict({"nb_temps_intensite_1" : 0}, ** map_mesures_modif[str(descripteur["mesure_courante"]) + '.2'])

        if "mesure_non_lue" not in descripteur :
          gc.disable()
          self.afficher(descripteur, scheduler)
        else:
          del descripteur["mesure_non_lue"]
          descripteur["afficher_passage"] = -1

        if "prochaine_mesure" in descripteur:
          descripteur["mesure_courante"] = descripteur["prochaine_mesure"]
          del descripteur["prochaine_mesure"]
        else:
          descripteur["mesure_courante"] += 1

        if "prochain_passage" in descripteur:
          descripteur["passage_reprise_courant"] = descripteur["prochain_passage"]
          del descripteur["prochain_passage"]

      else:
        while(self.etat == "pause"):
          self.wait()

    self.afficher_fin()
    scheduler.enter(temps_affichage_fin, 1, self.wait, ())
    scheduler.run()

    gc.enable()
    self.etat = "stop"


  def afficher(self, descripteur, scheduler):

    tempo_en_seconde = 60 / float(descripteur["tempo"])

    scheduler.enter(0,3,self.afficher_mesure,(descripteur["mesure_courante"],))
    if ("afficher_passage" in descripteur):
      if descripteur["afficher_passage"] == -1:
        scheduler.enter(0,3,self.afficher_passage,(-1,))
      else:
        scheduler.enter(0,3,self.afficher_passage,(descripteur["passage_reprise_courant"],))

    if ("label" in descripteur):
      scheduler.enter(0, 2, self.afficher_partie, (descripteur["label"],))
      descripteur["partie_courante"] = descripteur["label"]
      del descripteur["label"]

    for t in range(1, descripteur["temps_par_mesure"] + 1):

      scheduler.enter((t - 1) * tempo_en_seconde, 1, self.afficher_temps, (descripteur["temps_par_mesure"], t))

      if ("temps_debut_intensite_"+str(t) in descripteur):
        if (descripteur["nb_temps_intensite_"+str(t)] > 0):
          if (descripteur["intensite_courante"] == -1):
            intensite_base = descripteur["intensite_"+str(t)] - descripteur["nb_temps_intensite_"+str(t)]
            if intensite_base < 0: intensite_base = 0
            descripteur["intensite_courante"] = intensite_base
          else:
            descripteur["intensite_courante"] += (descripteur["intensite_"+str(t)] - descripteur["intensite_courante"]) / descripteur["nb_temps_intensite_"+str(t)]
          prochain_temps = (t % descripteur["temps_par_mesure"]) + 1
          descripteur["intensite_"+str(prochain_temps)] = descripteur["intensite_"+str(t)]
          descripteur["temps_debut_intensite_"+str(prochain_temps)] = True
          descripteur["nb_temps_intensite_"+str(prochain_temps)] = descripteur["nb_temps_intensite_"+str(t)] - 1
        else:
          descripteur["intensite_courante"] = descripteur["intensite_"+str(t)]
        del descripteur["intensite_"+str(t)]
        del descripteur["temps_debut_intensite_"+str(t)]
        del descripteur["nb_temps_intensite_"+str(t)]
        scheduler.enter((t - 1) * tempo_en_seconde, 1, self.afficher_intensite, (descripteur["intensite_courante"],))

      if ("temps_alerte_" + str(t) in descripteur):
        scheduler.enter((t - 1) * tempo_en_seconde, 1, self.afficher_alerte, (descripteur["couleur_alerte_" + str(t)],))
        if (descripteur["couleur_alerte_" + str(t)] != -1) and ("temps_alerte_" + str(t+1) not in descripteur):
          descripteur["temps_alerte_" + str(t+1)] = True
          descripteur["couleur_alerte_" + str(t+1)] = -1
        del descripteur["temps_alerte_" + str(t)]
        del descripteur["couleur_alerte_" + str(t)]

      if ("temps_debut_armature_" + str(t) in descripteur):
        scheduler.enter((t - 1) * tempo_en_seconde, 2, self.afficher_armature, (descripteur["alteration_" + str(t)],))
        descripteur["alteration_courante"] = descripteur["alteration_" + str(t)]
        del descripteur["temps_debut_armature_" + str(t)]
        del descripteur["alteration_" + str(t)]

      if (self.etat == "stop"):
        return 0
      else:
        while(self.etat == "pause"):
          self.wait()

    scheduler.enter(descripteur["temps_par_mesure"] * tempo_en_seconde, 1, self.wait, ())

    scheduler.run()


  def afficher_logo(self):
    image = Image.open(chemin_images + 'ema logo.png')
    image.load()
    self.matrix.SetImage(image.im.id, 0, 0)


  def afficher_decompte(self, temps_par_mesure, tempo, scheduler):
    tempo_en_seconde = 60 / float(tempo)
    for i in range(2):
      for t in range(temps_par_mesure):
        scheduler.enter(t * tempo_en_seconde, 1, self.afficher_decompte_aux, (t + 1,))
      scheduler.enter(temps_par_mesure * tempo_en_seconde, 1, self.wait, ())
      scheduler.run()
    image = Image.open(chemin_images + '64_32_black.png')
    image.load()
    scheduler.enter(0, 1, self.matrix.SetImage, (image.im.id, 0, 0))
    scheduler.run()


  def afficher_decompte_aux(self, numero):
    image = Image.open(chemin_images + 'd' + str(numero) + '.png')
    image.load()
    self.matrix.SetImage(image.im.id, 0, 0)


  def wait(self):
    return 0


  def afficher_fin(self):
    image = Image.open(chemin_images + 'fin.png')
    image.load()
    self.matrix.SetImage(image.im.id, 0, 0)


  def afficher_temps(self, temps_par_mesure, temps_courant):
    imageh = Image.open(chemin_images + 'a' + str(temps_par_mesure)+'_'+str(temps_courant)+'_h.png')
    imageh.load()
    imageg = Image.open(chemin_images + 'a' + str(temps_par_mesure)+'_'+str(temps_courant)+'_g.png')
    imageg.load()
    imaged = Image.open(chemin_images + 'a' + str(temps_par_mesure)+'_'+str(temps_courant)+'_d.png')
    imaged.load()
    imageb = Image.open(chemin_images + 'a' + str(temps_par_mesure)+'_'+str(temps_courant)+'_b.png')
    imageb.load()
    self.matrix.SetImage(imageh.im.id, pos_temps_h[0], pos_temps_h[1])
    self.matrix.SetImage(imageg.im.id, pos_temps_g[0], pos_temps_g[1])
    self.matrix.SetImage(imaged.im.id, pos_temps_d[0], pos_temps_d[1])
    self.matrix.SetImage(imageb.im.id, pos_temps_b[0], pos_temps_b[1])


  def afficher_mesure(self, mesure_courante):
    centaine = -1
    if mesure_courante >= 100:
      centaine = mesure_courante % 1000 / 100
    dizaine = -1
    if mesure_courante >= 10:
      dizaine = mesure_courante % 100 / 10
    unite = mesure_courante % 10
    if centaine == -1:
      image = Image.open(chemin_images + '8_8_black.png')
    else:
      image = Image.open(chemin_images + 'mesure' + str(centaine)+'.png')
    image.load()
    self.matrix.SetImage(image.im.id, pos_centaine[0], pos_centaine[1])
    if dizaine == -1:
      image = Image.open(chemin_images + '8_8_black.png')
    else:
      image = Image.open(chemin_images + 'mesure' + str(dizaine)+'.png')
    image.load()
    self.matrix.SetImage(image.im.id, pos_dizaine[0], pos_dizaine[1])
    image = Image.open(chemin_images + 'mesure' + str(unite)+'.png')
    image.load()
    self.matrix.SetImage(image.im.id, pos_unite[0], pos_unite[1])


  def afficher_passage(self, passage):
    if passage != -1:
      image = Image.open(chemin_images + 'passage' + str(passage)+'.png')
    else:
      image = Image.open(chemin_images + '8_8_black.png')
    image.load()
    self.matrix.SetImage(image.im.id, pos_passage[0], pos_passage[1])


  def afficher_intensite(self, intensite):
    if int(intensite) == -1:
      image = Image.open(chemin_images + '16_16_black.png')
    else:
      image = Image.open(chemin_images + 'intensite' + str(int(round(intensite)))+'.png')
    image.load()
    self.matrix.SetImage(image.im.id, pos_intensite[0], pos_intensite[1])


  def afficher_partie(self, partie):
    if partie == '-1':
      image = Image.open(chemin_images + '8_8_black.png')
    else:
      image = Image.open(chemin_images + 'partie' + partie +'.png')
    image.load()
    self.matrix.SetImage(image.im.id, pos_partie[0], pos_partie[1])


  def afficher_alerte(self, alerte):
    if alerte == -1:
      image = Image.open(chemin_images + '8_8_black.png')
    else:
      image = Image.open(chemin_images + 'alerte' + str(alerte) +'.png')
    image.load()
    self.matrix.SetImage(image.im.id, pos_alerte[0], pos_alerte[1])


  def afficher_armature(self, armature):
    if armature < 0:
      image1 = Image.open(chemin_images + 'armature' + str(-armature) + '.png')
      image2 = Image.open(chemin_images + 'bemol.png')
    elif armature > 0:
      image1 = Image.open(chemin_images + 'armature' + str(armature) + '.png')
      image2 = Image.open(chemin_images + 'diese.png')
    else:
      image1 = Image.open(chemin_images + '8_8_black.png')
      image2 = Image.open(chemin_images + '8_8_black.png')
    image1.load()
    image2.load()
    self.matrix.SetImage(image1.im.id, pos_armature_chiffre[0], pos_armature_chiffre[1])
    self.matrix.SetImage(image2.im.id, pos_armature_symb[0], pos_armature_symb[1])

  def get_etat(self):
    return self.etat

  def set_etat(self, etat):
    self.etat = etat








import Image
import ImageDraw
import sched
import time
import gc
from rgbmatrix import Adafruit_RGBmatrix

chemin_images = "drawable/"

temps_affichage_logo = 2

pos_temps = (8,0)
pos_intensite = (16, 8)
pos_passage = (0, 24)
pos_armature_symb = (56, 16)
pos_armature_chiffre = (48, 16)
pos_centaine = (40, 0)
pos_dizaine = (48, 0)
pos_unite = (56, 0)
pos_partie = (40, 8)
pos_alerte = (40, 24)

class afficheur :

  def __init__(self,mat):
		self.matrix = mat

  def lire(self, m, mesure_debut_lecture, passage_debut_lecture, mesure_fin_lecture, passage_fin_lecture):

    afficher_logo()

    gc.disable()

    map_mesures_modif = m.copy()
    descripteur = map_mesures_modif["1.1"]
    descripteur["mesure_courante"] = 1
    descripteur["passage_reprise_courant"] = 1
    descripteur["intensite_courante"] = -1

    while(descripteur["mesure_courante"], descripteur["passage_reprise_courant"]) != (mesure_debut_lecture, passage_debut_lecture):
      if (str(descripteur["mesure_courante"]) + '.' + str(descripteur["passage_reprise_courant"])) in map_mesures_modif :
        descripteur.update(map_mesures_modif[str(descripteur["mesure_courante"]) + '.' + str(descripteur["passage_reprise_courant"])])
      if "prochaine_mesure" in descripteur:
        descripteur["mesure_courante"] = descripteur["prochaine_mesure"]
      else:
        descripteur["mesure_courante"] += 1
      if "prochain_passage" in descripteur:
        descripteur["passage_reprise_courant"] = descripteur["prochain_passage"]

    scheduler = sched.scheduler(time.time, time.sleep)
    scheduler.enter(temps_affichage_logo, 1, wait, ())
    scheduler.run()

    afficher_decompte(descripteur["temps_par_mesure"], descripteur["tempo"], scheduler)

    while(descripteur["mesure_courante"], descripteur["passage_reprise_courant"]) != (mesure_fin_lecture + 1, passage_fin_lecture):
      if (str(descripteur["mesure_courante"]) + '.' + str(descripteur["passage_reprise_courant"])) in map_mesures_modif :
        descripteur.update(map_mesures_modif[str(descripteur["mesure_courante"]) + '.' + str(descripteur["passage_reprise_courant"])])
      if "mesure_non_lue" not in descripteur :
        afficher(descripteur, scheduler)
      if "prochaine_mesure" in descripteur:
        descripteur["mesure_courante"] = descripteur["prochaine_mesure"]
      else:
        descripteur["mesure_courante"] += 1
      if "prochain_passage" in descripteur:
        descripteur["passage_reprise_courant"] = descripteur["prochain_passage"]

    afficher_fin()

    gc.enable()


  def afficher(self, descripteur, scheduler):

    tempo_en_seconde = 60 / float(descripteur["tempo"])

    scheduler.enter(0,3,afficher_mesure,(descripteur["mesure_courante"],))
    scheduler.enter(0,3,afficher_passage,(descripteur["passage_reprise_courant"],))

    for t in range(1, descripteur["temps_par_mesure"] + 1):

      scheduler.enter((t - 1) * tempo_en_seconde, 1, afficher_temps, (descripteur["temps_par_mesure"], t))

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
          descripteur["intensite_courante"] = descripteur["intensite"]
        del descripteur["intensite_"+str(t)]
        del descripteur["temps_debut_intensite_"+str(t)]
        del descripteur["nb_temps_intensite_"+str(t)]
        scheduler.enter((t - 1) * tempo_en_seconde, 1, afficher_intensite, (descripteur["intensite_courante"],))

      if ("temps_alerte_" + str(t) in descripteur):
        scheduler.enter((t - 1) * tempo_en_seconde, 1, afficher_alerte, (descripteur["couleur_alerte_" + str(t)],))
        if (descripteur["couleur_alerte_" + str(t)] != -1) and ("temps_alerte_" + str(t+1) not in descripteur):
          descripteur["temps_alerte_" + str(t+1)] = True
          descripteur["couleur_alerte_" + str(t+1)] = -1
        del descripteur["temps_alerte_" + str(t)]
        del descripteur["couleur_alerte_" + str(t)]

      if ("temps_debut_armature_" + str(t) in descripteur):
        scheduler.enter((t - 1) * tempo_en_seconde, 1, afficher_armature, (descripteur["alteration_" + str(t)],))
        del descripteur["temps_debut_armature_" + str(t)]
        del descripteur["alteration_" + str(t)]

    scheduler.enter(descripteur["temps_par_mesure"] * tempo_en_seconde, 1, wait, ())

    scheduler.run()


  def afficher_logo(self):
    image = Image.open(chemin_images + 'ema logo.png')
    self.matrix.SetImage(image.im.id, 0, 0)


  def afficher_decompte(self, temps_par_mesure, tempo, scheduler):
    tempo_en_seconde = 60 / float(tempo)
    image = Image.open(chemin_images + '64*32_black.png')
    scheduler.enter(t * tempo_en_seconde, 1, self.matrix.SetImage, (image.im.id, 0, 0))
    for t in range(temps_par_mesure):
      image = Image.open(chemin_images + str(temps_par_mesure - t) + '.png')
      scheduler.enter(t * tempo_en_seconde, 1, self.matrix.SetImage, (image.im.id, 0, 0))
    scheduler.enter(temps_par_mesure * tempo_en_seconde, 1, wait, ())
    scheduler.run()

  def wait(self):
    return 0

  def afficher_d_helper(self, d):
    print time.time(), "decompte", d

  def afficher_fin(self):
    image = Image.open(chemin_images + 'fin.png')
    self.matrix.SetImage(image.im.id, 0, 0)


  def afficher_temps(self, temps_par_mesure, temps_courant):
    image = Image.open(chemin_images + 'a' + str(temps_par_mesure)+'_'+str(temps_courant)+'.png')
    self.matrix.SetImage(image.im.id, pos_temps[0], pos_temps[1])


  def afficher_mesure(self, mesure_courante):
    centaine = -1
    if mesure_courante >= 100:
      centaine = mesure_courante % 1000 / 100
    dizaine = -1
    if mesure_courante >= 10:
      dizaine = mesure_courante % 100 / 10
    unite = mesure_courante % 10
    if centaine == -1:
      image = Image.open(chemin_images + '8*8_black.png')
    else:
      image = Image.open(chemin_images + 'mesure' + str(centaine)+'.png')
    self.matrix.SetImage(image.im.id, pos_centaine[0], pos_centaine[1])
    if dizaine == -1:
      image = Image.open(chemin_images + '8*8_black.png')
    else:
      image = Image.open(chemin_images + 'mesure' + str(dizaine)+'.png')
    self.matrix.SetImage(image.im.id, pos_dizaine[0], pos_dizaine[1])
    image = Image.open(chemin_images + 'mesure' + str(unite)+'.png')
    self.matrix.SetImage(image.im.id, pos_unite[0], pos_unite[1])


  def afficher_passage(self, passage):
    image = Image.open(chemin_images + 'mesure' + str(passage)+'.png')# remplacer mesure par passage quand il y aura des images specifiques
    self.matrix.SetImage(image.im.id, pos_passage[0], pos_passage[1])


  def afficher_intensite(self, intensite):
    image = Image.open(chemin_images + 'intensite' + str(int(round(intensite)))+'.png')
    self.matrix.SetImage(image.im.id, pos_intensite[0], pos_intensite[1])


  def afficher_partie(self, partie):
    if alerte == '-1':
      image = Image.open(chemin_images + '8*8_black.png')
    else:
      image = Image.open(chemin_images + partie +'.png')
    self.matrix.SetImage(image.im.id, pos_partie[0], pos_partie[1])


  def afficher_alerte(self, alerte):
    if alerte == -1:
      image = Image.open(chemin_images + '8*8_black.png')
    else:
      image = Image.open(chemin_images + 'alerte' + str(alerte) +'.png')
    self.matrix.SetImage(image.im.id, pos_alerte[0], pos_alerte[1])


  def afficher_armature(self, armature):
    if armature < 0:
      image1 = Image.open(chemin_images + 'armature' + (-armature) + '.png')
      image2 = Image.open(chemin_images + 'bemol.png')
    elif armature > 0:
      image1 = Image.open(chemin_images + 'armature' + armature + '.png')
      image2 = Image.open(chemin_images + 'diese.png')
    else:
      image1 = Image.open(chemin_images + '8*8_black.png')
      image2 = Image.open(chemin_images + '8*8_black.png')
    self.matrix.SetImage(image1.im.id, pos_armature_chiffre[0], pos_armature_chiffre[1])
    self.matrix.SetImage(image2.im.id, pos_armature_symb[0], pos_armature_symb[1])


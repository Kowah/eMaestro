import MySQLdb as sql

class LecteurBDD:

  def __init__(self, h, u, p, d):
    self.host = h
    self.user = u
    self.passwd = p
    self.database = d

  def getMesureFin(self, id_musique):
    db = sql.connect(self.host, self.user, self.passwd, self.database)
    with db:
      cur = db.cursor()

      cur.execute('SELECT nb_mesures FROM Musique WHERE id_musique = '+str(id_musique))
      mesure_fin = cur.fetchall()[0][0]
      return mesure_fin

  def getDicoVariationTemps(self, id_musique):
    db = sql.connect(self.host, self.user, self.passwd, self.database)
    with db:
      cur = db.cursor()

      cur.execute('SELECT mesure_debut, passage_reprise, temps_par_mesure, tempo, unite_pulsation FROM VariationTemps WHERE id_musique = '+str(id_musique))
      infos = cur.fetchall()

      return {str(m) + '.'+ str(p) : {"temps_par_mesure" : tpm, "tempo" : t, "unite_pulsation" : u} for (m,p,tpm,t,u) in infos}

  def getDicoVariationIntensite(self, id_musique):
    db = sql.connect(self.host, self.user, self.passwd, self.database)
    with db:
      cur = db.cursor()

      cur.execute('SELECT mesure_debut, passage_reprise, temps_debut, nb_temps, intensite FROM VariationIntensite WHERE id_musique = '+str(id_musique))
      infos = cur.fetchall()

      return {str(m) + '.'+ str(p) : {"temps_debut_intensite_"+str(tdi) : True, "nb_temps_intensite_"+str(tdi) : nt, "intensite_"+str(tdi) : i} for (m,p,tdi,nt,i) in infos}

  def getDicoPartie(self, id_musique):
    db = sql.connect(self.host, self.user, self.passwd, self.database)
    with db:
      cur = db.cursor()

      cur.execute('SELECT mesure_debut, label FROM Partie WHERE id_musique = '+str(id_musique))
      infos = cur.fetchall()

      return {str(m) + '.1' : {"label" : l} for (m,l) in infos}

  def getDicoMesuresNonLues(self, id_musique):
    db = sql.connect(self.host, self.user, self.passwd, self.database)
    with db:
      cur = db.cursor()

      cur.execute('SELECT mesure_debut, passage_reprise, mesure_fin_ou_temps_debut FROM Evenement WHERE id_musique = '+str(id_musique)+' AND type_evenement = 0')
      infos = cur.fetchall()

      d1 = {str(m) + '.'+ str(p) : {"prochaine_mesure" : mf, "mesure_non_lue" : True} for (m,mf,p) in infos}
      d2 = {str(mf) + '.'+ str(p) : {"mesure_non_lue" : True} for (m,mf,p) in infos}
      d1.update(d2)
      if d1 == None:
        d1 = {}
      return d1

  def getDicoReprise(self, id_musique):
    db = sql.connect(self.host, self.user, self.passwd, self.database)
    with db:
      cur = db.cursor()

      cur.execute('SELECT mesure_fin_ou_temps_debut, mesure_debut FROM Evenement WHERE id_musique = '+str(id_musique)+' AND type_evenement = 1')
      infos = cur.fetchall()

      d1 = {str(mf) + '.1' : {"prochaine_mesure" : md, "prochain_passage" : 2} for (mf,md) in infos}
      d2 = {str(mf) + '.2' : {"prochain_passage" : 1} for (mf,md) in infos}
      d1.update(d2)
      if d1 == None:
        d1 = {}
      return d1

  def getDicoAlerte(self, id_musique):
    db = sql.connect(self.host, self.user, self.passwd, self.database)
    with db:
      cur = db.cursor()

      cur.execute('SELECT mesure_debut, passage_reprise, mesure_fin_ou_temps_debut, information_evenement FROM Evenement WHERE id_musique = '+str(id_musique)+' AND type_evenement = 2')
      infos = cur.fetchall()

      return {str(m) + '.'+ str(p) : {"temps_alerte_" + str(t) : True, "couleur_alerte_" + str(t) : int(c)} for (m,p,t,c) in infos}

  def getDicoVariationRythme(self, id_musique):
    db = sql.connect(self.host, self.user, self.passwd, self.database)
    with db:
      cur = db.cursor()

      cur.execute('SELECT mesure_debut, passage_reprise, mesure_fin_ou_temps_debut, information_evenement FROM Evenement WHERE id_musique = '+str(id_musique)+' AND type_evenement = 3')
      infos = cur.fetchall()

      return {str(m) + '.'+ str(p) : {"temps_debut_variation_rythme_" + str(t) : True, "taux_de_variation_rythme_" + str(t) : tv} for (m,p,t,tv) in infos}

  def getDicoSuspension(self, id_musique):
    db = sql.connect(self.host, self.user, self.passwd, self.database)
    with db:
      cur = db.cursor()

      cur.execute('SELECT mesure_debut, passage_reprise, mesure_fin_ou_temps_debut, information_evenement FROM Evenement WHERE id_musique = '+str(id_musique)+' AND type_evenement = 4')
      infos = cur.fetchall()

      return {str(m) + '.'+ str(p) : {"temps_suspension_" + str(t) : True, "duree_suspension_" + str(t) : s} for (m,p,t,s) in infos}

  def getDicoArmature(self, id_musique):
    db = sql.connect(self.host, self.user, self.passwd, self.database)
    with db:
      cur = db.cursor()

      cur.execute('SELECT mesure_debut, passage_reprise, mesure_fin_ou_temps_debut, information_evenement FROM Evenement WHERE id_musique = '+str(id_musique)+' AND type_evenement = 5')
      infos = cur.fetchall()

      return {str(m) + '.'+ str(p) : {"temps_debut_armature_" + str(t) : True, "alteration_" + str(t) : int(a)} for (m,p,t,a) in infos}



















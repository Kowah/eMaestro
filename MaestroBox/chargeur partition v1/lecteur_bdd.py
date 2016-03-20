import MySQLdb as sql

class LecteurBDD:

  def __init__(self, h, u, p, d):
    self.host = h
    self.user = u
    self.passwd = p
    self.database = d

  def getMesureFin(self, id_musique):
#    db = sql.connect(host="localhost", user="tristan", passwd="1234", db="partition_emaestro_db")
    db = sql.connect(self.host, self.user, self.passwd, self.database)
    with db:
      cur = db.cursor()

      cur.execute('SELECT nb_mesures FROM Musique WHERE id_musique = '+str(id_musique))
      mesure_fin = cur.fetchall()[0][0]
      return mesure_fin

  def getInfosMusique(self, id_musique):
    db = sql.connect(self.host, self.user, self.passwd, self.database)
    with db:
      cur = db.cursor()

      cur.execute('SELECT mesure_debut, temps_par_mesure, tempo FROM Variation_temps WHERE id_musique = '+str(id_musique)+' ORDER BY mesure_debut ASC')
      infos = cur.fetchall()
      return infos


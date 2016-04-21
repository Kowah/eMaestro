#import sqlite3 as sql
import MySQLdb as sql

db = sql.connect(host="localhost", user="tristan", passwd="1234", db='emaestro')
#db = sql.connect('partition_emaestro.db')

with db:
  cur = db.cursor()

#  cur.execute("create table Musique(id_musique int, nom text, nb_mesures int)")
#  cur.execute("insert into Musique values(1, 'musique1', 58)")
#  cur.execute("insert into Musique values(2, 'musique2', 117)")

#  cur.execute("create table VariationTemps(id_variation_temps int, id_musique int, mesure_debut int, temps_par_mesure int, tempo int, unite_pulsation int, passage_reprise int)")
#  cur.execute("insert into VariationTemps values(1,1,1,4,125,4,1)")
#  cur.execute("insert into VariationTemps values(2,1,17,7,50,4,1)")
#  cur.execute("insert into VariationTemps values(3,1,25,4,50,4,1)")
#  cur.execute("insert into VariationTemps values(4,1,32,5,33,4,1)")
#  cur.execute("insert into VariationTemps values(5,2,1,5,88,4,1)")
#  cur.execute("insert into VariationTemps values(6,2,42,5,100,4,1)")
#  cur.execute("insert into VariationTemps values(7,2,85,4,100,4,1)")
#  cur.execute("insert into VariationTemps values(8,1,44,4,30,4,1)")

  cur.execute("create table VariationIntensite(id_variation_intensite int, id_musique int, mesure_debut int, temps_debut int, nb_temps int, intensite int, passage_reprise int)")



  cur.execute("create table Partie(id_partie int, id_musique int, mesure_debut int, label text)")

  cur.execute("create table Evenement(id_evenement int, id_musique int, type_evenement int, mesure_debut int, mesure_fin_ou_temps_debut int, passage_reprise int, information_evenement float)")


#db.commit()

#cur.close()

#db.close()

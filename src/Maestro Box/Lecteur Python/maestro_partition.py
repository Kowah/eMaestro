import MySQLdb as sql

db = sql.connect(host="localhost", user="guest", passwd="guest", db="emaestro")

with db:
  cur = db.cursor()

  cur.execute("create table musique(id_musique int,name text, nb_mesures int, nb_pulsation int, unite_pulsation int, nb_temps_mesure int)")
  cur.execute("insert into musique values(1,'musique', 888, -1,-1,-1)")
  cur.execute("insert into musique values(2,'chanson', 117,-1,-1,-1)")

  cur.execute("create table VarTemps(idVarTemps int, id_musique int, mesure_debut int, temps_par_mesure int, tempo int)")
  cur.execute("insert into VarTemps values(1,1,1,4,125)")
  cur.execute("insert into VarTemps values(2,1,157,7,50)")
  cur.execute("insert into VarTemps values(3,1,465,4,50)")
  cur.execute("insert into VarTemps values(4,1,612,5,33)")
  cur.execute("insert into VarTemps values(5,2,1,5,88)")
  cur.execute("insert into VarTemps values(6,2,42,5,100)")
  cur.execute("insert into VarTemps values(7,2,85,4,100)")
  cur.execute("insert into VarTemps values(8,1,224,4,30)")


#db.commit()

#cur.close()

#db.close()

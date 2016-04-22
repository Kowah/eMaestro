import sqlite3 as sql
#import MySQLdb as sql

db = sql.connect(host="localhost", user="tristan", passwd="1234", db='emaestro')
#db = sql.connect('partition_emaestro.db')

with db:
  cur = db.cursor()
  cur.execute('SELECT * FROM Musique')
  selection = cur.fetchall()
  print selection
  cur.execute('SELECT * FROM Variation_temps')
  selection = cur.fetchall()
  print selection

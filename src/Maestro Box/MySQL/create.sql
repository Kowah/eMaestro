DROP TABLE IF EXISTS Musique;
DROP TABLE IF EXISTS VariationTemps;
DROP TABLE IF EXISTS VariationIntensite;
DROP TABLE IF EXISTS Partie; 
DROP TABLE IF EXISTS MesuresNonLues;
DROP TABLE IF EXISTS Reprise;
DROP TABLE IF EXISTS Alerte;
DROP TABLE IF EXISTS VariationRythme;
DROP TABLE IF EXISTS Suspension;
DROP TABLE IF EXISTS Armature;
DROP TABLE IF EXISTS Evenement;

CREATE TABLE Musique (id_musique INTEGER PRIMARY KEY , nom TEXT, nb_mesures INTEGER);
CREATE TABLE VariationTemps (id_variation_temps INTEGER PRIMARY KEY , id_musique INTEGER, mesure_debut INTEGER, temps_par_mesure INTEGER, tempo INTEGER, unite_pulsation INTEGER);
CREATE TABLE VariationIntensite (id_variation_intensite INTEGER PRIMARY KEY, id_musique INTEGER, mesure_debut INTEGER, temps_debut INTEGER, nb_temps INTEGER, intensite INTEGER);
CREATE TABLE Partie (id_partie INTEGER PRIMARY KEY, id_musique INTEGER, mesure_debut INTEGER, label TEXT);
CREATE TABLE MesuresNonLues (id_mesures_non_lues INTEGER PRIMARY KEY, id_musique INTEGER, mesure_debut INTEGER, mesure_fin INTEGER, passage_reprise INTEGER);
CREATE TABLE Reprise (id_reprise INTEGER PRIMARY KEY, id_musique INTEGER, mesure_debut INTEGER, mesure_fin INTEGER);
CREATE TABLE Alerte (id_alerte INTEGER PRIMARY KEY, id_musique INTEGER, mesure_debut INTEGER, temps_debut INTEGER, couleur INTEGER, passage_reprise INTEGER);
CREATE TABLE VariationRythme (id_variation_rythme INTEGER PRIMARY KEY, id_musique INTEGER, mesure_debut INTEGER, temps_debut INTEGER, taux_de_variation FLOAT, passage_reprise INTEGER);
CREATE TABLE Suspension (id_suspension INTEGER PRIMARY KEY, id_musique INTEGER, mesure INTEGER, temps INTEGER, duree INTEGER, passage_reprise INTEGER);
CREATE TABLE Armature (id_armature INTEGER PRIMARY KEY, id_musique INTEGER, mesure_debut INTEGER, temps_debut INTEGER, alteration INTEGER, passage_reprise INTEGER);
CREATE TABLE Evenement (id_evenement INTEGER PRIMARY KEY, id_musique INTEGER, flag INTEGER, mesure_debut INTEGER, arg2 INTEGER, passage_reprise INTEGER, arg3 INTEGER);

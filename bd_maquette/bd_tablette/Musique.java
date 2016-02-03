package com.example.projet;

public class Musique {
	private int id;
	private String name;
	private int nb_mesures;
	private int tempo;
	
	public Musique(String name, int mesure, int tempo){
		this.id = -1; //-1 tant que pas sauvegardé dans la base
		this.name = name;
		this.nb_mesures = mesure;
		this.tempo = tempo;
	}
	public Musique(String name, int mesure, int tempo, int id){
		this.id = id; 
		this.name = name;
		this.nb_mesures = mesure;
		this.tempo = tempo;
	}
	
	public void setId(int nId){
		this.id = nId;
	}
	
	public int getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String newName){
		this.name = newName;
	}
	
	public int getMesures(){
		return nb_mesures;
	}
	
	public void setMesusre(int newMesure){
		this.nb_mesures = newMesure;
	}
	
	public void setTempo(int newTempo){
		this.tempo = newTempo;
	}
	
	public int getTempo(){
		return this.tempo;
	}
	
}

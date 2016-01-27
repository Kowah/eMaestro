package com.example.projet;

public class Musique {
	
	private String name;
	private int nb_mesures;
	
	public Musique(String name, int mesure){
		this.name = name;
		this.nb_mesures = mesure;

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
	
}

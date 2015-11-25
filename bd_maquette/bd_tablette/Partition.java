package com.example.projet;

public class Partition {
	
	private String name;
	private int mesures;
	private int rythmes;
	
	public Partition(String name, int mesure, int rythme){
		this.name = name;
		this.mesures = mesure;
		this.rythmes = rythme;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String newName){
		this.name = newName;
	}
	
	public int getMesures(){
		return mesures;
	}
	
	public void setMesusre(int newMesure){
		this.mesures = newMesure;
	}
	
	public int getRythme(){
		return rythmes;
	}
	
	public void setRythme(int newRythme){
		this.rythmes = newRythme;
	}
}

package emulateur;
import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import BDD.db.*;
import BDD.to.MesuresNonLues;
import BDD.to.Musique;
import BDD.to.Reprise;
import BDD.to.VariationTemps;
import util.Pair;
import util.Triple;

public class Chargeur_partition {

	private final Activity mainActivity;
	DataBaseManager bdd ;

	Chargeur_partition(Activity main, DataBaseManager bdd){
		mainActivity = main;
		this.bdd = bdd;
	}

	private int id_partition_chargee; //pour savoir quelle partition est en memoire, c'est l'id_musique de cette derniere dans la bdd
	private Map<String, Integer> images_ephemeres = new HashMap();
	private List<Pair<String, Integer>> liste_de_lecture = new ArrayList<Pair<String, Integer>>();
	private Map<Integer, Integer> map_mesures_temps = new HashMap<Integer, Integer>();

	private void creer_images_ephemeres (int temps_par_mesure){
		String key = "";
		for(int i = 1; i <= temps_par_mesure; i++){
			key = String.valueOf(temps_par_mesure) + "." + String.valueOf(i);
			int id = mainActivity.getResources().getIdentifier("a"+temps_par_mesure+"_"+i, "drawable", mainActivity.getPackageName());
			this.images_ephemeres.put(key, id);
		}
		//ajout de l'image de fin
		int id = mainActivity.getResources().getIdentifier("fin", "drawable", mainActivity.getPackageName());
		images_ephemeres.put("fin", id);

		//ajout des nombres de decompte
		for(int n=1; n<=8; n++){
			int idN = mainActivity.getResources().getIdentifier("d"+n, "drawable", mainActivity.getPackageName());
			images_ephemeres.put("d"+n, idN);
		}
	}
	
	private int creer_liste_lecture (VariationTemps varI, int mesure_fin, int nb_temps){
		int next_nb_temps = nb_temps;
		int tempo = varI.getTempo();
		String key = "";
		int temps_par_mesure = varI.getTemps_par_mesure();
		int mesure_debut = varI.getMesure_debut();
		for (int i = mesure_debut; i <= mesure_fin; i++){
			this.map_mesures_temps.put(i, next_nb_temps+1); //debug j'ai rajoute le +1 pour test avec la nouvelle mapCercle
			for (int j = 1; j <= temps_par_mesure; j++){
				key = String.valueOf(temps_par_mesure) + "." + String.valueOf(j);
				this.liste_de_lecture.add(new Pair<String, Integer>(key, tempo));
				next_nb_temps++;
			}
		}
		return next_nb_temps;
	}

	public void charger_partition (int id_musique){
		if (id_musique != this.id_partition_chargee){
			int mesure_fin;
			int infos_size;
			int nb_temps = 0;

			List<VariationTemps> infos = bdd.getVariationsTemps(bdd.getMusique(id_musique));
			mesure_fin = bdd.getMusique(id_musique).getNb_mesure();
			infos_size = infos.size();
			for(int i = 0; i < infos_size; i++){
				VariationTemps varI = infos.get(i);
				if (!images_ephemeres.containsKey(varI.getTemps_par_mesure() + ".1")){
					creer_images_ephemeres(varI.getTemps_par_mesure());
				}
				if (i == infos_size - 1){
					nb_temps = creer_liste_lecture(varI, mesure_fin, nb_temps);
				}
				else{
					nb_temps = creer_liste_lecture(varI, infos.get(i+1).getMesure_debut() - 1, nb_temps);
				}
			}

			this.id_partition_chargee = id_musique;
		}
	}
	
	public int get_id_partition_chargee(){return id_partition_chargee;}
	public Map<String, Integer> get_images_ephemeres(){return images_ephemeres;}
	public List<Pair<String, Integer>> get_liste_de_lecture(){return liste_de_lecture;}
	public Map<Integer, Integer> get_map_mesures_temps(){return map_mesures_temps;}



}

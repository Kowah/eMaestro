package emaestro.testtelecommande;
import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chargeur_partition {

	private final Activity mainActivity;

	Chargeur_partition(Activity main){
		mainActivity = main;
	}

	private int id_partition_chargee; //pour savoir quelle partition est en memoire, c'est l'id_musique de cette derniere dans la bdd
	private Map<String, Integer> images_ephemeres = new HashMap();
	private List<Pair<String, Integer>> liste_de_lecture = new ArrayList<Pair<String, Integer>>();
	private Map<Integer, Integer> map_mesures_temps = new HashMap<Integer, Integer>();

	private void creer_images_ephemeres (int temps_par_mesure){
		//Map<String, String> images_permanentes = Collection_images.getMap();
		String key = "";
		String chemin = "";
		for(int i = 1; i <= temps_par_mesure; i++){
			key = String.valueOf(temps_par_mesure) + "." + String.valueOf(i);
			chemin = "emaestro.testtelecommande:drawable/a"+temps_par_mesure+"_"+i;
			int id = mainActivity.getResources().getIdentifier(chemin, null, null);
			this.images_ephemeres.put(key, id);
		}
	}
	
	private int creer_liste_lecture (Triple<Integer, Integer, Integer> triple, int mesure_fin, int nb_temps){
		int next_nb_temps = nb_temps;
		int tempo = triple.getRight();
		String key = "";
		int temps_par_mesure = triple.getMid();
		int mesure_debut = triple.getLeft();
		for (int i = mesure_debut; i <= mesure_fin; i++){
			this.map_mesures_temps.put(i, next_nb_temps);
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
			int mesure_fin = 0;
			Triple<Integer, Integer, Integer> triple_temp = new Triple<Integer, Integer, Integer>(0,0,0);
			int infos_size = 0;
			int nb_temps = 0;
			List<Triple<Integer, Integer, Integer>> infos = new ArrayList<Triple<Integer, Integer, Integer>>();
			mesure_fin = LecteurBDD.getMesureFin(id_musique);
			infos = LecteurBDD.getInfosMusique(id_musique);
			infos_size = infos.size();
			for(int i = 0; i < infos_size; i++){
				triple_temp = infos.get(i);
				if (!images_ephemeres.containsKey(triple_temp.getMid().toString() + ".1")){
					creer_images_ephemeres(triple_temp.getMid());
				}
				if (i == infos_size - 1){
					nb_temps = creer_liste_lecture(triple_temp, mesure_fin, nb_temps);
				}
				else{
					nb_temps = creer_liste_lecture(triple_temp, infos.get(i+1).getLeft() - 1, nb_temps);
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

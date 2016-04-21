
public class Testeur {
	public static void main( String args[] ){
		int id_musique = 2;
		System.out.println(LecteurBDD.getMesureFin(id_musique));
		System.out.println(LecteurBDD.getInfosMusique(id_musique));
		Chargeur_partition partition = new Chargeur_partition();
		partition.charger_partition(id_musique);
		System.out.println(partition.get_id_partition_chargee());
		System.out.println(partition.get_images_ephemeres());
		System.out.println(partition.get_liste_de_lecture());
		System.out.println(partition.get_liste_de_lecture().size());
		System.out.println(partition.get_map_mesures_temps());
		System.out.println(partition.get_map_mesures_temps().size());
	}
}

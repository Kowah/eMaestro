package database;

/**
 * Created by Guillaume on 08/03/2016.
 */
public class RemplissageBdd {

    public static void remplirDonneesTest(MaestroBDD bdd){
        bdd.clearTable("Variation_temps");
        bdd.clearTable("Musique");
        bdd.insertMusique(1, 4);
        bdd.insertVariationTemps(1,1,1,4,120);
        bdd.insertVariationTemps(2,1,2,8,120);
        bdd.insertVariationTemps(3,1,4,8,120);
        bdd.insertVariationTemps(4,1,3,4,120);
    }
}

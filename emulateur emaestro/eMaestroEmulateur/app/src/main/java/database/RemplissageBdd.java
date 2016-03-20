package database;

/**
 * Created by Guillaume on 08/03/2016.
 */
public class RemplissageBdd {

    public static void remplirDonneesTest(MaestroBDD bdd){
        bdd.clearTable("Variation_temps");
        bdd.clearTable("Musique");
        bdd.insertMusique(1, "premier Morceau", 35);
        bdd.insertMusique(2, "deuxieme Morceau", 20);
        bdd.insertVariationTemps(1,1,1,2,120);
        bdd.insertVariationTemps(2,1,5,3,120);
        bdd.insertVariationTemps(3,1,10,4,120);
        bdd.insertVariationTemps(4,1,15,5,120);
        bdd.insertVariationTemps(5,1,20,6,120);
        bdd.insertVariationTemps(6,1,25,7,240);
        bdd.insertVariationTemps(7,1,30,8,240);
        bdd.insertVariationTemps(8,2,1,4,120);
    }
}

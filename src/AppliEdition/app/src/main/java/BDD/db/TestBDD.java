package BDD.db;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import BDD.to.Alertes;
import BDD.to.Armature;
import BDD.to.Evenement;
import BDD.to.MesuresNonLues;
import BDD.to.Musique;
import BDD.to.Partie;
import BDD.to.Reprise;
import BDD.to.Suspension;
import BDD.to.VarRythmes;
import BDD.to.VariationIntensite;
import BDD.to.VariationTemps;

/**
 * Created by guillaume on 21/04/16.
 */
public class TestBDD {

        Context c;
        public TestBDD(Context c) {
            this.c = c;
        }
        public boolean run() {
            boolean res = true;
            if(!testMusique()){
                res = false;
                System.out.println("Les testes de la base de données sur les Musiques ne sont pas passés");
            }
            if(!testVariationTemps()){
                res = false;
                System.out.println("Les testes de la base de données sur les VariationTemps ne sont pas passés");
            }
            if(!testVariationIntensite()){
                res = false;
                System.out.println("Les testes de la base de données sur les VariationsIntensite ne sont pas passés");
            }
            if(!testPartie()){
                res = false;
                System.out.println("Les testes de la base de données sur les Parties ne sont pas passés");
            }
            if(!testEvenement()){
                res = false;
                System.out.println("Les testes de la base de données sur les Evenements ne sont pas passés");
            }
            return res;
        }

    public boolean testMusique() {
        DataBaseManager bdd= new DataBaseManager(c);
        List<Musique> musique = new ArrayList<>();
        List<Musique> res;
        bdd.open();
        for(int i = 0; i < 100; i++){
            musique.add(new Musique("Test"+i,i));
        }

        //Test SAVE
        for(Musique m:musique){
            m.setId((int) bdd.save(m));
        }
        res = bdd.getMusiques();
        for(Musique m : musique){
            if(!res.contains(m)) {
                System.out.println("TEST echoué: Musique:SAVE");
                bdd.close();
                return false;
            }
        }
        //Test UPDATE
        for (Musique m:musique){
            m.setNb_mesure(-1);
            bdd.update(m);
        }
        res = bdd.getMusiques();
        for(Musique m : musique) {
            if (!res.contains(m)) {
                System.out.println("TEST echoué: Musique:UPDATE");
                bdd.close();
                return false;
            }
        }
        //Test DELETE
        for (Musique m:musique){
            bdd.delete(m);
        }
        res = bdd.getMusiques();
        for(Musique m : musique) {
            if (res.contains(m)) {
                System.out.println("TEST echoué: Musique:DELETE");
                bdd.close();
                return false;
            }
        }
        bdd.close();
        return true;
    }

    public boolean testVariationTemps() {
        DataBaseManager bdd= new DataBaseManager(c);
        List<Musique> musique = new ArrayList<>();
        List<VariationTemps> variationT = new ArrayList<>();
        List<VariationTemps> res;
        bdd.open();
        //On crée 10 musiques d'ident 0 <= i < 10
        for(int i = 0; i < 10; i++){
            musique.add(new Musique(i,"Test"+i,i+1));
        }
        for(int i = 0; i < 100; i++){
            variationT.add(new VariationTemps(i % 10, i+1, i+2, i+3, i+4));
        }
        //Save
        for(VariationTemps v:variationT){
            v.setIdVarTemps((int) bdd.save(v));
        }
        for(Musique m: musique) {
            res = bdd.getVariationsTemps(m);
            for (VariationTemps v : variationT) {
                if (v.getIDmusique() == m.getId() && !res.contains(v)) {
                    System.out.println("TEST echoué: VariationTemps:SAVE");
                    bdd.close();
                    return false;
                }
            }
        }
        //Update
         for(VariationTemps v:variationT){
             v.setMesure_debut(-1);
             bdd.update(v);
         }
        for(Musique m: musique) {
            res = bdd.getVariationsTemps(m);
            for (VariationTemps v : variationT) {
                if (v.getIDmusique() == m.getId() && !res.contains(v)) {
                    System.out.println("TEST echoué: VariationTemps:UPDATE");
                    bdd.close();
                    return false;
                }
            }
        }
        //Delete
        for(Musique m:musique){
            bdd.deleteVarTemps(m);
        }
        for(Musique m: musique) {
            res = bdd.getVariationsTemps(m);
            if (!res.isEmpty()) {
                System.out.println("TEST echoué: VariationTemps:DELETE");
                bdd.close();
                return false;
                }
        }
        bdd.close();
        return true;
    }

    public boolean testVariationIntensite() {
        DataBaseManager bdd= new DataBaseManager(c);
        List<Musique> musique = new ArrayList<>();
        List<VariationIntensite> variationI = new ArrayList<>();
        List<VariationIntensite> res;
        bdd.open();
        //On crée 10 musiques d'ident 0 <= i < 10
        for(int i = 0; i < 10; i++){
            musique.add(new Musique(i,"Test"+i,i));
        }
        for(int i = 0; i < 100; i++){
            variationI.add(new VariationIntensite(i % 10, i+1, i+2, i+3, i+4));
        }
        //Save
        for(VariationIntensite v:variationI){
            v.setIdVarIntensite((int) bdd.save(v));
        }
        for(Musique m: musique) {
            res = bdd.getVariationsIntensite(m);
            for (VariationIntensite v : variationI) {
                if (v.getIdMusique() == m.getId() && !res.contains(v)) {
                    System.out.println("TEST echoué: VariationIntensite:SAVE");
                    bdd.close();
                    return false;
                }
            }
        }
        //Update
        for(VariationIntensite v:variationI){
            v.setMesureDebut(-1);
            bdd.update(v);
        }
        for(Musique m: musique) {
            res = bdd.getVariationsIntensite(m);
            for (VariationIntensite v : variationI) {
                if (v.getIdMusique() == m.getId() && !res.contains(v)) {
                    System.out.println("TEST echoué: VariationIntensite:UPDATE");
                    bdd.close();
                    return false;
                }
            }
        }
        //Delete
        for(Musique m:musique){
            bdd.deleteVarIntensite(m);
        }
        for(Musique m: musique) {
            res = bdd.getVariationsIntensite(m);
            if (!res.isEmpty()) {
                System.out.println("TEST echoué: VariationIntensite:DELETE");
                bdd.close();
                return false;
            }
        }
        bdd.close();
        return true;
    }

    public boolean testPartie() {
        DataBaseManager bdd= new DataBaseManager(c);
        List<Musique> musique = new ArrayList<>();
        List<Partie> parties = new ArrayList<>();
        List<Partie> res;
        bdd.open();
        //On crée 10 musiques d'ident 0 <= i < 10
        for(int i = 0; i < 10; i++){
            musique.add(new Musique(i,"Test"+i,i));
        }
        for(int i = 0; i < 100; i++){
            parties.add(new Partie(i % 10, i+1, "Test"+i));
        }
        //Save
        for(Partie p:parties){
            p.setId((int) bdd.save(p));
        }
        for(Musique m: musique) {
            res = bdd.getParties(m);
            for (Partie p : parties) {
                if (p.getIdMusique() == m.getId() && !res.contains(p)) {
                    System.out.println("TEST echoué: Partie:SAVE");
                    bdd.close();
                    return false;
                }
            }
        }
        //Update
        for(Partie p:parties){
            p.setMesure_debut(-1);
            bdd.update(p);
        }
        for(Musique m: musique) {
            res = bdd.getParties(m);
            for (Partie p : parties) {
                if (p.getIdMusique() == m.getId() && !res.contains(p)) {
                    System.out.println("TEST echoué: Partie:UPDATE");
                    bdd.close();
                    return false;
                }
            }
        }
        //Delete
        for(Musique m:musique){
            bdd.deletePartie(m);
        }
        for(Musique m: musique) {
            res = bdd.getParties(m);
            if (!res.isEmpty()) {
                System.out.println("TEST echoué: Partie:DELETE");
                bdd.close();
                return false;
            }
        }
        bdd.close();
        return true;
    }
    public boolean testEvenement() {
        DataBaseManager bdd= new DataBaseManager(c);
        List<Musique> musique = new ArrayList<>();
        List<Evenement> events = new ArrayList<>();
        List<Evenement> res;
        bdd.open();
        //On crée 10 musiques d'ident 0 <= i < 10
        for(int i = 0; i < 10; i++){
            musique.add(new Musique(i,"Test"+i,i));
        }
        for(int i = 0; i < 100; i++){
            events.add(new Evenement(i % 10, i+1, i+2,i+3, i+4, i+5));
        }
        //Save
        for(Evenement e:events){
            e.setId((int) bdd.save(e));
        }
        for(Musique m: musique) {
            res = bdd.getEvenement(m);
            for (Evenement e : events) {
                if (e.getIdMusique() == m.getId() && !res.contains(e)) {
                    System.out.println("TEST echoué: Evenement:SAVE");
                    bdd.close();
                    return false;
                }
            }
        }
        //Update
        for(Evenement e:events){
            e.setMesure_debut(-1);
            e.setFlag(-1);
            e.setArg2(-1);
            e.setArg3(-1);
            e.setPassage_reprise(-1);
            bdd.update(e);
        }
        for(Musique m: musique) {
            res = bdd.getEvenement(m);
            for (Evenement e : events) {
                if (e.getIdMusique() == m.getId() && !res.contains(e)) {
                    System.out.println("TEST echoué: Evenement:UPDATE");
                    bdd.close();
                    return false;
                }
            }
        }
        //Delete
        for(Musique m:musique){
            bdd.deleteEvenement(m);
        }
        for(Musique m: musique) {
            res = bdd.getEvenement(m);
            if (!res.isEmpty()) {
                System.out.println("TEST echoué: Evenement:DELETE");
                bdd.close();
                return false;
            }
        }
        bdd.close();
        return true;
    }


}

package BDD.to;


public class Evenement {
    protected int id = -1;
    protected int idMusique;
    protected int flag;
    protected int mesure_debut;
    protected int arg2;
    protected int passage_reprise;
    protected float arg3;

    public Evenement(){}

    public Evenement(int id, int idMusique, int flag, int mesure_debut, int arg2, int passage_reprise, float arg3){
        this.id = id;
        this.idMusique = idMusique;
        this.flag = flag;
        this.mesure_debut = mesure_debut;
        this.arg2 = arg2;
        this.passage_reprise = passage_reprise;
        this.arg3 = arg3;
    }

    public Evenement(int idMusique, int flag, int mesure_debut, int arg2, int passage_reprise, int arg3){
        this.idMusique = idMusique;
        this.flag = flag;
        this.mesure_debut = mesure_debut;
        this.arg2 = arg2;
        this.passage_reprise = passage_reprise;
        this.arg3 = arg3;
    }

    public void setId(int id){this.id = id;}
    public void setFlag(int flag){this.flag = flag;}
    public void setIdMusique(int idMusique) {this.idMusique = idMusique;}
    public void setMesure_debut(int mesure_debut) {this.mesure_debut = mesure_debut;}
    public void setArg2(int arg2) {this.arg2 = arg2;}
    public void setPassage_reprise(int passage_reprise){this.passage_reprise=passage_reprise;}
    public void setArg3(int arg3) {this.arg3 = arg3;}
    public int getId() {return id;}
    public int getFlag(){return flag;}
    public int getIdMusique() {return idMusique;}
    public int getMesure_debut() {return mesure_debut;}
    public int getArg2(){return arg2;}
    public int getPassage_reprise(){return passage_reprise;}
    public float getArg3() {return arg3;}

}

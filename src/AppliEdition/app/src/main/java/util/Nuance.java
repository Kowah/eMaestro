package util;

/**
 * Created by Guillaume on 15/04/2016.
 */
public enum Nuance {
    NEUTRE("neutre"),
    FORTISSISSIMO("fortississimo"),
    FORTISSIMO("fortissimo"),
    FORTE("forte"),
    MEZZOFORTE("mezzo forte"),
    MEZZOPIANO("mezzo piano"),
    PIANO("piano"),
    PIANISSIMO("pianissimo"),
    PIANISSISSIMO("pianississimo");

    private String text = "";

    public String getText() {
        return text;
    }

    Nuance(String text){
        this.text = text;
    }


    public static String[] getAllNuances(){
        String[] nuancesTab = {"neutre","fortississimo","fortissimo","forte","mezzo forte","mezzo piano","piano","pianissimo","pianississimo"};
        return  nuancesTab;
    }

    public static Nuance convertStringToNuance(String s){
        Nuance nuance = null;
        switch(s){
            case "neutre":
                nuance = NEUTRE;
                break;
            case "fortississimo":
                nuance = FORTISSISSIMO;
                break;
            case "fortissimo":
                nuance = FORTISSIMO;
                break;
            case "forte":
                nuance = FORTE;
                break;
            case "mezzo forte":
                nuance = MEZZOFORTE;
                break;
            case "mezzo piano":
                nuance = MEZZOPIANO;
                break;
            case "piano":
                nuance = PIANO;
                break;
            case "pianissimo":
                nuance = PIANISSIMO;
                break;
            case "pianississimo":
                nuance = PIANISSISSIMO;
                break;
        }
        return nuance;
    }
}

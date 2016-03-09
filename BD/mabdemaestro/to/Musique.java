package fr.istic.univ_rennes1.diengadama.mabdemaestro.to;

import android.os.Parcel;
import android.os.Parcelable;

public class Musique implements Parcelable {
    private int id;
    private String name;
    private int nb_mesure;
    private int nb_pulsation;
    private int unite_pulsation;
    private int nb_temps_mesure;

    public Musique() {
        super();
    }

    public Musique(int id, String name, int nb_mesure, int nb_pulsation, int unite_pulsation, int nb_temps_mesure) {
        super();
        this.id = id;
        this.name = name;
        this.nb_mesure = nb_mesure;
        this.nb_pulsation = nb_pulsation;
        this.unite_pulsation = unite_pulsation;
        this.nb_temps_mesure = nb_temps_mesure;
    }

    public Musique(String name, int nb_mesure, int nb_pulsation, int unite_pulsation, int nb_temps_mesure) {
        this.name = name;
        this.nb_mesure = nb_mesure;
        this.nb_pulsation = nb_pulsation;
        this.unite_pulsation = unite_pulsation;
        this.nb_temps_mesure = nb_temps_mesure;
    }

    private Musique(Parcel in) {
        super();
        this.id = in.readInt();
        this.name = in.readString();
        this.nb_mesure = in.readInt();
        this.nb_pulsation = in.readInt();
        this.unite_pulsation = in.readInt();
        this.nb_temps_mesure = in.readInt();
    }

    public int getId() {return id;}

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNb_mesure(){
        return this.nb_mesure;
    }

    public void setNb_mesure(int nb_mesure){
        this.nb_mesure = nb_mesure;
    }

    public int getNb_pulsation(){
        return this.nb_pulsation;
    }
    public void setNb_pulsation(int nb_pulsation){
        this.nb_pulsation = nb_pulsation;
    }

    public int getUnite_pulsation(){
        return this.nb_pulsation;
    }
    public void setUnite_pulsation(int unite_pulsation){
        this.unite_pulsation = unite_pulsation;
    }
    public int getNb_temps_mesure(){
        return this.nb_temps_mesure;
    }
    public void setNb_temps_mesure(int nb_temps_mesure){
        this.nb_mesure = nb_temps_mesure;
    }

    @Override
    public String toString() {
        return  name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(getId());
        parcel.writeString(getName());
    }

    public static final Parcelable.Creator<Musique> CREATOR = new Parcelable.Creator<Musique>() {
        public Musique createFromParcel(Parcel in) {
            return new Musique(in);
        }

        public Musique[] newArray(int size) {
            return new Musique[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Musique other = (Musique) obj;
        if (id != other.id)
            return false;
        return true;
    }
}

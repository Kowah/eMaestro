package BDD.to;

import android.os.Parcel;
import android.os.Parcelable;

public class Musique implements Parcelable {
    private int id;
    private String name;
    private int nb_mesure;



    public Musique() {
        super();
    }

    public Musique(int id, String name, int nb_mesure) {
        super();
        this.id = id;
        this.name = name;
        this.nb_mesure = nb_mesure;

    }

    public Musique(String name, int nb_mesure) {
        this.id = -1;
        this.name = name;
        this.nb_mesure = nb_mesure;
    }

    private Musique(Parcel in) {
        super();
        this.id = in.readInt();
        this.name = in.readString();
        this.nb_mesure = in.readInt();
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

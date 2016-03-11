package BDD.to;

import android.os.Parcel;
import android.os.Parcelable;

public class Symboles implements Parcelable {
    private int id;
    private String name;

    public Symboles() {
        super();
    }

    public Symboles(int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public Symboles(String name) {
        this.name = name;
    }

    private Symboles(Parcel in) {
        super();
        this.id = in.readInt();
        this.name = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
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

    public static final Parcelable.Creator<Symboles> CREATOR = new Parcelable.Creator<Symboles>() {
        public Symboles createFromParcel(Parcel in) {
            return new Symboles(in);
        }

        public Symboles[] newArray(int size) {
            return new Symboles[size];
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
        Symboles other = (Symboles) obj;
        if (id != other.id)
            return false;
        return true;
    }
}

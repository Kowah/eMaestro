package BDD.to;

import android.os.Parcel;
import android.os.Parcelable;

public class Evenement implements Parcelable {
	/*this.idMusique = idMusique;
	this.idSymbole= idSymbole;
	this.nTemps = nTemps;
	this.couleur = couleur;*/

	private int id;
	//private int idSymbole;//name
	private int nTemps; //dateOfBirth;
	private String couleur;//double salary;
	private Musique Musique;
	private Symboles Symboles;

	public Evenement() {
		super();
	}

	private Evenement(Parcel in) {
		super();
		this.id = in.readInt();
		//this.idSymbole = in.readInt();
		this.Symboles = in.readParcelable(Symboles.class.getClassLoader());
		//this.dateOfBirth = new Date(in.readLong());
		this.nTemps = in.readInt();
		this.couleur = in.readString();
		this.Musique = in.readParcelable(Musique.class.getClassLoader());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	//Retourne un symbole
	public Symboles getSymboles() {
		return Symboles;
	}
	//Affecter un symbole
	public void setSymboles(Symboles Symboles) {
		this.Symboles = Symboles;
	}
	/*
        public Date getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(Date dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }
    */
	public int getnTemps() {
		return nTemps;
	}

	public void setnTemps(int nTemps) {
		this.nTemps = nTemps;
	}

	public String getCouleur() {
		return couleur;
	}
	public void setCouleur(String  couleur) {
		this.couleur = couleur;
	}

	public Musique getMusiquet() {
		return Musique;
	}
	public void setMusique(Musique Musique) {
		this.Musique = Musique;
	}

	@Override
	public String toString() {
		return "Evenement [id=" + id + ", Symbole=" + Symboles + ", Couleur=" + couleur
				+ ", nTemp= "+ nTemps + ", Musique="
				+ Musique + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Evenement other = (Evenement) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(getId());
		parcel.writeParcelable(getSymboles(), flags);
		//parcel.writeInt(getIdSymbole());
		//parcel.writeLong(getDateOfBirth().getTime());
		parcel.writeInt(getnTemps());
		parcel.writeString(getCouleur());
		parcel.writeParcelable(getMusiquet(), flags);
	}

	public static final Parcelable.Creator<Evenement> CREATOR = new Parcelable.Creator<Evenement>() {
		public Evenement createFromParcel(Parcel in) {
			return new Evenement(in);
		}

		public Evenement[] newArray(int size) {
			return new Evenement[size];
		}
	};

}

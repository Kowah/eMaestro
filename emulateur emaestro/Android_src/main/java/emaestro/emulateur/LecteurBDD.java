package emaestro.emulateur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LecteurBDD{
	public static int getMesureFin(int id_musique)
	  {
	    Connection c = null;
	    Statement stmt = null;
	    int mesure_fin = 0;
	    try {

	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:test.db");
	      c.setAutoCommit(false);

	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery("SELECT nb_mesures FROM Musique WHERE id_musique = ".concat(String.valueOf(id_musique)));
	      while ( rs.next() ) {
	         mesure_fin = rs.getInt("nb_mesures");
	      }
	      rs.close();
	      stmt.close();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    return mesure_fin;
	  }
	
	public static List<Triple<Integer, Integer, Integer>> getInfosMusique(int id_musique)
	  {
	    Connection c = null;
	    Statement stmt = null;
	    List<Triple<Integer, Integer, Integer>> infos = new ArrayList<Triple<Integer, Integer, Integer>>();
	    
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:partition_emaestro.db");
	      c.setAutoCommit(false);

	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT mesure_debut, temps_par_mesure, tempo FROM Variation_temps WHERE id_musique = ".concat(String.valueOf(id_musique)) );
	      while ( rs.next() ) {
	         int mesure_debut = rs.getInt("mesure_debut");
	         int temps_par_mesure  = rs.getInt("temps_par_mesure");
	         int tempo = rs.getInt("tempo");
	         infos.add(new Triple<Integer, Integer, Integer>(mesure_debut, temps_par_mesure, tempo));
	      }
	      rs.close();
	      stmt.close();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    return infos;
	  }
}
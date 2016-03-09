package BDD.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import BDD.to.Symboles;

import java.util.ArrayList;
import java.util.List;


public class SymbolesDAO extends EvenementDBDAO {

    private static final String WHERE_ID_EQUALS = DataBaseHelper.ID_COLUMN
            + " =?";

    public SymbolesDAO(Context context) {
        super(context);
    }

    public long save(Symboles symbole) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.NAME_COLUMN, symbole.getName());

        return database.insert(DataBaseHelper.SYMBOLE_TABLE, null, values);
    }

    public long update(Symboles symbole) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.NAME_COLUMN, symbole.getName());

        long result = database.update(DataBaseHelper.SYMBOLE_TABLE, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(symbole.getId()) });
        Log.d("Update Result:", "=" + result);
        return result;

    }

    public int deleteMusic(Symboles Symbole) {
        return database.delete(DataBaseHelper.SYMBOLE_TABLE,
                WHERE_ID_EQUALS, new String[] { Symbole.getId() + "" });
    }

    public List<Symboles> getSymboles() {
        List<Symboles> symbole = new ArrayList<Symboles>();
        Cursor cursor = database.query(DataBaseHelper.SYMBOLE_TABLE,
                new String[] { DataBaseHelper.ID_COLUMN,
                        DataBaseHelper.NAME_COLUMN }, null, null, null, null,
                null);

        while (cursor.moveToNext()) {
            Symboles Symboles = new Symboles();
            Symboles.setId(cursor.getInt(0));
            Symboles.setName(cursor.getString(1));
            symbole.add(Symboles);
        }
        return symbole;
    }

    public void loadSymboles() {
        Symboles symbole0  = new Symboles("Symbole 1");
        Symboles symbole1   = new Symboles("Symbole 2");
        Symboles symbole2   = new Symboles("Symbole 3");
        Symboles symbole3   = new Symboles("Symbole 4");
        Symboles symbole4   = new Symboles("Symbole 5");
        Symboles symbole5   = new Symboles("Symbole 6");
        Symboles symbole6   = new Symboles("Symbole 7");
        Symboles symbole7 = new Symboles("Symbole 8");
        Symboles symbole8 = new Symboles("Symbole 9");
        Symboles symbole9 = new Symboles("Symbole 10");
        Symboles symbole10 = new Symboles("Symbole 11");
        Symboles symbole11 = new Symboles("Symbole 12");
        Symboles symbole12 = new Symboles("Symbole 13");
        Symboles symbole13 = new Symboles("Symbole 14");
        Symboles symbole14 = new Symboles("Indication de tempo");

        List<Symboles> symboles = new ArrayList<Symboles>();
        symboles.add(symbole0);
        symboles.add(symbole1);
        symboles.add(symbole2);
        symboles.add(symbole3);
        symboles.add(symbole4);
        symboles.add(symbole5);
        symboles.add(symbole6);
        symboles.add(symbole7);
        symboles.add(symbole8);
        symboles.add(symbole9);
        symboles.add(symbole10);
        symboles.add(symbole11);
        symboles.add(symbole12);
        symboles.add(symbole13);
        symboles.add(symbole14);

        symboles = TriAlphabetique((ArrayList<Symboles>) symboles);
        for (Symboles symb : symboles) {
            ContentValues values = new ContentValues();
            values.put(DataBaseHelper.NAME_COLUMN, symb.getName());
            database.insert(DataBaseHelper.SYMBOLE_TABLE, null, values);
        }
    }

    public  ArrayList<Symboles>TriAlphabetique(ArrayList<Symboles> pListe)
    {
        Integer i, j, min;
        Symboles symb;
        for(i = 0; i < pListe.size(); i++)
        {
            min = i;
            for(j = i; j < pListe.size(); j++)
            {
                if(pListe.get(min).getName().compareTo(pListe.get(j).getName()) > 0)
                {
                    min = j;
                }
            }
            symb = pListe.get(i);
            pListe.set(i, pListe.get(min));
            pListe.set(min, symb);
        }
        return pListe;
    }
}

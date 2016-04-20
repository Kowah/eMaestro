package BDD.db;

import android.content.Context;

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
public class Debug {

        Context c;
        public Debug(Context c) {
            this.c = c;
        }
        public void run() {
            DataBaseManager db = new DataBaseManager(c);
            db.open();
            db.clean();
            Musique m = new Musique("TEST", 20);
            long res = db.save(new Musique("TEST", 20));
            m.setId((int) res);
            db.save(new VariationTemps((int) res, 5, 5, 5, 5));
            db.save(new VariationIntensite((int) res, 6, 6, 6, 6));
            db.save(new Partie((int) res, 4, "DEBUG"));
            long t1 = db.save(new MesuresNonLues((int) res, 1, 1, 1));
            long t2 = db.save(new Reprise((int) res, 2, 2));
            long t7 = db.save(new Reprise((int) res, 2222, 2222));
            long t3 = db.save(new Alertes((int) res, 3, 3, 3, 3));
            long t4 = db.save(new VarRythmes((int) res, 7, 7, 7, 7));
            long t5 = db.save(new Suspension((int) res, 8, 8, 8, 8));
            long t6 = db.save(new Armature((int) res, 9, 9, 9, 9));
            db.getParties(m);
            db.getMesuresNonLues(m);
            db.getVarRythmes(m);
            for (Evenement r : db.getEvenement(m)) {
                System.out.println("ASAP" + r.getArg2());
            }
            db.close();

		/*db.delete(new MesuresNonLues((int) t1, (int) res,11,12,13));
		db.delete(new Reprise ((int) t2, (int) res, 21, 22));
		db.delete(new Alertes((int) t3, (int) res, 31, 32, 33, 34));
		db.delete(new VarRythmes((int) t4, (int) res, 71, 72, 73, 74));
		db.delete(new Suspension((int) t5, (int) res, 81, 82, 83, 84));
		db.delete(new Armature((int) t6,(int) res, 91, 92, 93, 94));
		db.deleteArmature(m);
		db.deleteReprise(m);
		db.deleteReprise(m);
		db.close();
		CatalogueDAO bdd = new CatalogueDAO(this);
		bdd.open();
		bdd.save(new Musique((int) res, "TEST", 20));
		bdd.close();
		*/
        }
}

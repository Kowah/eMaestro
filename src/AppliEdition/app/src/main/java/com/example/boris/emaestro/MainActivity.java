package com.example.boris.emaestro;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

import BDD.db.CatalogueDAO;
import BDD.db.DataBaseManager;
import BDD.to.Alertes;
import BDD.to.Armature;
import BDD.to.MesuresNonLues;
import BDD.to.Musique;
import BDD.to.Partie;
import BDD.to.Reprise;
import BDD.to.Suspension;
import BDD.to.VarRythmes;
import BDD.to.VariationIntensite;
import BDD.to.VariationTemps;
import emulateur.EmulateurActivity;
import telecommande.Telecommande;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ecran_accueil);
		/*
		//DataBaseManager.connect(this);
		DataBaseManager db = new DataBaseManager(this);
		db.open();
		long res = db.save(new Musique("TEST",20));
		db.save(new VariationTemps((int) res,5,5,5,5));
		db.save(new VariationIntensite((int) res, 6,6,6,6));
		db.save(new Partie((int) res, 4, "DEBUG"));
		db.save(new MesuresNonLues((int) res,1,1,1));
		db.save(new Reprise((int) res, 2,2));
		db.save(new Alertes((int) res,3,3,3,3));
		db.save(new VarRythmes((int) res, 7,7,7,7));
		db.save(new Suspension((int) res, 8,8,8,8));
		db.save(new Armature((int) res, 9, 9, 9, 9));
		db.close();
		CatalogueDAO bdd = new CatalogueDAO(this);
		bdd.open();
		bdd.save(new Musique((int) res,"TEST",20));
		bdd.close();
		*/
		final ImageButton nouveau = (ImageButton) findViewById(R.id.nouveau);
		nouveau.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this, CreationMusiqueActivity.class);
				startActivity(intent);
			}
		});

		final Button synchro = (Button) findViewById(R.id.synchro);
		synchro.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DataBaseManager bd = new DataBaseManager(v.getContext());
				bd.open();
				List<Musique> m = bd.getMusiques();
				bd.close();

				CatalogueDAO bdd = new CatalogueDAO(v.getContext());
				bdd.open();
				bdd.clean();
				bdd.save(m);
				bdd.synchronizer();
				bdd.close();
			}
		});
		final ImageButton chercher = (ImageButton) findViewById(R.id.chercher);
		chercher.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, CatalogueActivity.class);
				startActivity(intent);
			}
		});

		final Button emulateur = (Button) findViewById(R.id.emulateur);
		emulateur.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, EmulateurActivity.class);
				startActivity(intent);
			}
		});

		final ImageButton telecommande = (ImageButton) findViewById(R.id.telecommande);
		telecommande.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this, Telecommande.class);
				startActivity(intent);
			}
		});
		final ImageButton reconnect = (ImageButton) findViewById(R.id.reconnect);
		reconnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DataBaseManager.connect(v.getContext());
			}
		});


	}
	 @Override
	protected void onDestroy(){
		 super.onDestroy();
		 DataBaseManager.disconnect(this);
	 }


	}

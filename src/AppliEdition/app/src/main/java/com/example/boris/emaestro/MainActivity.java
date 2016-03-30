package com.example.boris.emaestro;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.List;

import BDD.db.CatalogueDAO;
import BDD.db.DataBaseManager;

import BDD.to.Musique;
import BDD.to.VariationTemps;

import emulateur.EmulateurActivity;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ecran_accueil);
/*
		long m1,m2;
		CatalogueDAO db =new CatalogueDAO(this);
		DataBaseManager bdd = new DataBaseManager(this);
		bdd.open();
		bdd.clean();
		m1 = bdd.save(new Musique("Test1", 2));
		m2 = bdd.save(new Musique("Test2",10));
		bdd.update(new Musique((int) m1, "Test3", 99));

		long var = bdd.save((new VariationTemps((int) m2, 3, 3, 3, 3)));
		bdd.save((new VariationTemps((int) m1, 1, 2, 1,1)));
		bdd.save((new VariationTemps((int) m2, 1, 2, 1,1)));
		bdd.update((new VariationTemps((int) var,(int) m1,5,8,4,2)));

		db.open();;
		db.save(m1);
		db.save(m2);
		db.synchronizer();
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

				//TODO lancer l'appli telecommande
			}
		});


	}

	}

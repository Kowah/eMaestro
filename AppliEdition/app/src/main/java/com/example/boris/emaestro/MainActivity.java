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
		CatalogueDAO db =new CatalogueDAO(this);
		DataBaseManager bdd = new DataBaseManager(this);
		bdd.open();

		bdd.save(new Musique(1,"Test1",2));
		bdd.save(new Musique(2,"Test2",3));
		bdd.save((new VariationTemps(1,2,3,4,5)));
		bdd.save((new VariationTemps(2,1,4,4,4)));
		bdd.save((new VariationTemps(1,2,9,9,9)));

		db.open();;
		db.save(1);
		db.save(2);
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

		final ImageButton chercher = (ImageButton) findViewById(R.id.chercher);
		chercher.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO recherche dans catalogue à faire
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


	}

	}

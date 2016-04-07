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
import BDD.to.Musique;
import emulateur.EmulateurActivity;
import telecommande.Telecommande;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ecran_accueil);
		DataBaseManager.connect(this);

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

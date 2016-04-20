package com.example.boris.emaestro;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import BDD.db.CatalogueDAO;
import BDD.db.DataBaseManager;
import BDD.to.Evenement;
import BDD.to.Musique;
import emulateur.EmulateurActivity;
import telecommande.Telecommande;

public class MainActivity extends Activity {

	Context c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		c = this;
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
			int selected = -1;
			@Override
			public void onClick(View v) {
				Context c = v.getContext();

				WifiManager wifi = (WifiManager) v.getContext().getSystemService(v.getContext().WIFI_SERVICE);
				List<android.net.wifi.ScanResult> scan = wifi.getScanResults();

				final List<String> name = new ArrayList<String>();
				for (android.net.wifi.ScanResult s : scan) {
					//TODO: Garder que les reseaux commencant par maestro
					name.add(s.SSID);
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				LayoutInflater inflater = getLayoutInflater();
				View convertView = (View) inflater.inflate(R.layout.wifi, null);
				builder.setView(convertView);
				ListView lv = (ListView) convertView.findViewById(R.id.listWifi);

				lv.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
						// Affichage de la donnée sélectionnée dans un textview
						selected = position;
						System.out.println(id +"asap touche" + position);
					}
				});

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(),android.R.layout.simple_list_item_1,name.toArray(new String[name.size()]));
				lv.setAdapter(adapter);
				builder.setTitle("Wifi")

						.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								DataBaseManager.connect(getApplicationContext(), name.get(selected));
								System.out.println("asap OK");
							}
						})
						.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								//TODO:
								System.out.println("asap Cancel");
							}
						});
				builder.show();
			}


		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		DataBaseManager.disconnect(this);
	}
}

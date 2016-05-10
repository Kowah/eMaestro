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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import BDD.db.CatalogueDAO;
import BDD.db.DataBaseManager;
import BDD.db.TestBDD;
import emulateur.EmulateurActivity;
import telecommande.Telecommande;

public class MainActivity extends Activity {

	Context c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		c = this;
		setContentView(R.layout.ecran_accueil);
		/* Tests BDD
		TestBDD test = new TestBDD(c);
		if(!test.run()){
			System.out.println("Attention, il y'a une erreur dans la gestion de la BDD");
		}
		*/;

		final ImageButton nouveau = (ImageButton) findViewById(R.id.nouveau);
		nouveau.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this, CreationMusiqueActivity.class);
				startActivity(intent);
			}
		});

		final ImageButton synchro = (ImageButton) findViewById(R.id.synchro);
		synchro.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CatalogueDAO bdd = new CatalogueDAO(v.getContext());
				bdd.open();
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

		final ImageButton catalogue = (ImageButton) findViewById(R.id.catalogue);
		catalogue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ChangeCatalogueActivity.class);
				startActivity(intent);
			}
		});

		final ImageButton emulateur = (ImageButton) findViewById(R.id.emulateur);
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
		final ImageButton connectToMaestroBox = (ImageButton) findViewById(R.id.reconnect);
		connectToMaestroBox.setOnClickListener(new OnClickListener() {
			int selected = -1;
			String keyName = "Maestro";
			boolean enable = false;
			@Override
			public void onClick(View v) {
				WifiManager wifi = (WifiManager) v.getContext().getSystemService(v.getContext().WIFI_SERVICE);
				if(!wifi.isWifiEnabled()){
					Toast.makeText(getApplicationContext(), "Attention votre Wifi est désactivé, merci de l'activer pour continuer", Toast.LENGTH_SHORT).show();
				}
				if (wifi.isWifiEnabled()) {
					//Scans des réseaux disponibles
					List<android.net.wifi.ScanResult> scan = wifi.getScanResults();

					final List<String> name = new ArrayList<String>();
					name.clear();
					//Si le nom du reseau commence par keyname on le garde
					for (android.net.wifi.ScanResult s : scan) {
							if(s.SSID.length() >= keyName.length() && s.SSID.substring(0,keyName.length()).equals(keyName)) {
							name.add(s.SSID);
						}
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
						}
					});
					//Affiche les Maestrobox disponibles
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, name.toArray(new String[name.size()]));
					lv.setAdapter(adapter);
					builder.setTitle("MaestrBox disponibles : ")

							.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int id) {
									if(selected >= 0) {
										DataBaseManager.connect(getApplicationContext(), name.get(selected));
									}
									else{
										Toast.makeText(getApplicationContext(), "Aucune MaestroBox de selectionnée", Toast.LENGTH_SHORT).show();
									}
								}
							})
							.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int id) {
									//Rien à faire
								}
							});
					builder.show();
				}

			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		DataBaseManager.disconnect(this);
	}
}

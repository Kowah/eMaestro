package com.example.boris.emaestro;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ecran_accueil);
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
				//TODO
			//	Intent intent = new Intent(MainActivity.this, CatalogueActivity.class);
			//	startActivity(intent);
			}
		});

		final ImageButton telecommande = (ImageButton) findViewById(R.id.telecommande);
		telecommande.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, TelecommandeActivity.class);
				startActivity(intent);
			}
		});
	}

	}
/*

	EditText nom = null;
	EditText mesure = null;
	Button envoyer = null;
	Button clean = null;
	TextView result = null;
	MusiqueDAO db = new MusiqueDAO(this);
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		nom = (EditText) findViewById(R.id.nom);
		mesure = (EditText) findViewById(R.id.mesure);
		envoyer = (Button) findViewById(R.id.envoie);
		clean = (Button) findViewById(R.id.clean);
		result = (TextView) findViewById(R.id.result);
		db.open();
				
		envoyer.setOnClickListener(envoyerListener);
		clean.setOnClickListener(cleanListener);
		nom.addTextChangedListener(textWatcher);
	}

	private TextWatcher textWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
			result.setText("Bien continue :)");
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		
		@Override
		public void afterTextChanged(Editable s) {}
	};
	
	private OnClickListener envoyerListener = new OnClickListener() {
		@Override	
		public void onClick(View v)
		{
			String name = nom.getText().toString();
			String nb_mesures = mesure.getText().toString();
			int nb_mesure = stringToInt(nb_mesures);
			db.insert(new Musique(name, nb_mesure));
			result.setText(db.getMusique(name).getName()+"/"+db.getMusique(name).getMesures());
		}
	};
	private OnClickListener cleanListener = new OnClickListener() {
		@Override
		public void onClick(View v)
		{
			db.clean();
		}
	};
	
	private int stringToInt(String str){
		int res = 0;
		for(int i = 0; i < str.length(); i++) {
			res += (str.charAt(str.length()-i-1)-'0')*Math.pow(10, i);
			}
		return res;
	}
	*/



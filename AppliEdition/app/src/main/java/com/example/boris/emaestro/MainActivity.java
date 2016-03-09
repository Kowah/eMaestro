package com.example.boris.emaestro;

import android.content.Context;
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
				//TODO recherche dans catalogue à faire
			//	Intent intent = new Intent(MainActivity.this, CatalogueActivity.class);
			//	startActivity(intent);
			}
		});


	}

	}

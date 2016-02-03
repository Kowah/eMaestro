package com.example.projet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.text.Editable;
import android.text.TextWatcher;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	EditText nom = null;
	EditText mesure = null;
	EditText tempo = null;
	Button envoyer = null;
	Button clean = null;
	Button delete = null;
	Button print = null;
	TextView result = null;
	MusiqueDAO db = new MusiqueDAO(this);
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		nom = (EditText) findViewById(R.id.nom);
		mesure = (EditText) findViewById(R.id.mesure);
		tempo = (EditText) findViewById(R.id.tempo);
		envoyer = (Button) findViewById(R.id.envoie);
		clean = (Button) findViewById(R.id.clean);
		delete = (Button) findViewById(R.id.delete);
		print = (Button) findViewById(R.id.print);
		result = (TextView) findViewById(R.id.result);
		db.open();
				
		envoyer.setOnClickListener(envoyerListener);
		clean.setOnClickListener(cleanListener);
		delete.setOnClickListener(deleteListener);
		print.setOnClickListener(printListener);
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
			String tmp = mesure.getText().toString();
			int nb_mesure = stringToInt(tmp);
			tmp  = tempo.getText().toString();
			int _tempo = stringToInt(tmp);
			db.insert(new Musique(name, nb_mesure,_tempo));
			//Musique musique = db.getMusique(name);
			//result.setText("Id: "+musique.getId()+"/"+musique.getName()+"/"+musique.getMesures()+"/"+musique.getTempo());
			}
	};
	private OnClickListener cleanListener = new OnClickListener() {
		@Override
		public void onClick(View v)
		{
			db.clean();
		}
	};
	
	private OnClickListener deleteListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			db.delete(stringToInt(nom.getText().toString()));
		}
	};
	
	private OnClickListener printListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			result.setText(printMusiques(db.getAllMusique()));			
		}
	};
	
	private int stringToInt(String str){
		int res = 0;
		for(int i = 0; i < str.length(); i++) {
			res += (str.charAt(str.length()-i-1)-'0')*Math.pow(10, i);
			}
		return res;
	}
	
	private String printMusiques(List<Musique> musiques){
		Musique tmp_mus;
		String res = "";
		List<Musique> allMusiques = new ArrayList<>();
		allMusiques = musiques;
		Iterator<Musique> it = allMusiques.iterator();
		while(it.hasNext()){
			tmp_mus = it.next();
			res += "Id: "+tmp_mus.getId()+"/"+tmp_mus.getName()+"/"+tmp_mus.getMesures()+"/"+tmp_mus.getTempo()+"\n";
		}
		return res;
	}
	
}


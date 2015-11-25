package com.example.projet;

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

	EditText text = null;
	Button envoyer = null;
	Button clean = null;
	TextView result = null;
	PartitionDAO db = new PartitionDAO(this);
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		text = (EditText) findViewById(R.id.texte);
		envoyer = (Button) findViewById(R.id.envoie);
		clean = (Button) findViewById(R.id.clean);
		result = (TextView) findViewById(R.id.result);
		db.open();
				
		envoyer.setOnClickListener(envoyerListener);
		clean.setOnClickListener(cleanListener);
		text.addTextChangedListener(textWatcher);
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
			String name = text.getText().toString();
			db.insert(new Partition(name, 2, 5));
			result.setText(db.getPartiton(name).getName()+"/"+db.getPartiton(name).getMesures());
		}
	};
	private OnClickListener cleanListener = new OnClickListener() {
		@Override
		public void onClick(View v)
		{
			db.clean();
		}
	};
	
}


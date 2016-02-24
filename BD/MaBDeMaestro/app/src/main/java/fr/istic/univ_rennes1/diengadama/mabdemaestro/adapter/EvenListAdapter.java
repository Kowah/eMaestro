package fr.istic.univ_rennes1.diengadama.mabdemaestro.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import fr.istic.univ_rennes1.diengadama.mabdemaestro.R;
import fr.istic.univ_rennes1.diengadama.mabdemaestro.to.Evenement;
public class EvenListAdapter extends ArrayAdapter<Evenement> {

	private Context context;
	List<Evenement> evenements;

	private static final SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.ENGLISH);

	public EvenListAdapter(Context context, List<Evenement> evenements) {
		super(context, R.layout.list_item, evenements);
		this.context = context;
		this.evenements = evenements;
	}

	private class ViewHolder {
		TextView empIdTxt;
		TextView empNameTxt;
		TextView empDobTxt;//couleur
		TextView empSalaryTxt;//ntemps
		TextView empDeptNameTxt;
	}
//Compte le nbre d'évenements
	@Override
	public int getCount() {
		return evenements.size();
	}
//Retourne l'évenement à la position i
	@Override
	public Evenement getItem(int position) {
		return evenements.get(position);
	}
//L'indice du 1er évenement
	@Override
	public long getItemId(int position) {
		return 0;
	}
//Récupérer une vue d'évenements
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();

			holder.empIdTxt = (TextView) convertView
					.findViewById(R.id.txt_emp_id);
			holder.empNameTxt = (TextView) convertView
					.findViewById(R.id.txt_emp_name);
			holder.empDobTxt = (TextView) convertView
					.findViewById(R.id.txt_emp_dob);
			holder.empSalaryTxt = (TextView) convertView
					.findViewById(R.id.txt_emp_salary);
			holder.empDeptNameTxt = (TextView) convertView
					.findViewById(R.id.txt_emp_dept);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Evenement evenement = (Evenement) getItem(position);
		holder.empIdTxt.setText(evenement.getId() + "");
		//holder.empNameTxt.setText("Symbole : "+ evenement.getIdSymbole()+ "");
		//holder.empNameTxt.setText("Symbole : "+evenement.getSymboles ().getName()+ "");
		holder.empNameTxt.setText("Symbole : "+ evenement.getSymboles().getName()+ "");
		holder.empDobTxt.setText("Couleur : "+evenement.getCouleur() + "");
		holder.empSalaryTxt.setText("nTemps : "+evenement.getnTemps() + "");
		holder.empDeptNameTxt.setText("Musique : "+evenement.getMusiquet ().getName()+ "");
		//Convertion de la date sur la liste
		//holder.empDobTxt.setText(formatter.format(employee.getDateOfBirth()));
		return convertView;
	}
//Fonction qui ajoute un évenement
	@Override
	public void add(Evenement evenement) {
		evenements.add(evenement);
		notifyDataSetChanged();
		super.add(evenement);
	}

	@Override
	public void remove(Evenement evenement) {
		evenements.remove(evenement);
		notifyDataSetChanged();
		super.remove(evenement);
	}
}


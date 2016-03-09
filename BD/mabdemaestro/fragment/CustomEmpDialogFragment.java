package fr.istic.univ_rennes1.diengadama.mabdemaestro.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import fr.istic.univ_rennes1.diengadama.mabdemaestro.MainActivity;
import fr.istic.univ_rennes1.diengadama.mabdemaestro.R;
import fr.istic.univ_rennes1.diengadama.mabdemaestro.db.EvenementDAO;
import fr.istic.univ_rennes1.diengadama.mabdemaestro.db.MusiqueDAO;
import fr.istic.univ_rennes1.diengadama.mabdemaestro.db.SymbolesDAO;
import fr.istic.univ_rennes1.diengadama.mabdemaestro.to.Evenement;
import fr.istic.univ_rennes1.diengadama.mabdemaestro.to.Musique;
import fr.istic.univ_rennes1.diengadama.mabdemaestro.to.Symboles;

public class CustomEmpDialogFragment extends DialogFragment {

	// UI references
	//private EditText empNameEtxt;
	private Spinner empNameEtxt;
	private EditText empSalaryEtxt;
	private EditText empDobEtxt;
	private Spinner deptSpinner;
	private LinearLayout submitLayout;

	private Evenement evenement;

	EvenementDAO employeeDAO;
	ArrayAdapter<Symboles> adapterSymb;
	ArrayAdapter<Musique> adapter;

	public static final String ARG_ITEM_ID = "emp_dialog_fragment";

	private static final SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.ENGLISH);

	/*
	 * Callback used to communicate with EmpListFragment to notify the list adapter.
	 * MainActivity implements this interface and communicates with EmpListFragment.
	 */
	public interface CustomEmpDialogFragmentListener {
		void onFinishDialog();
	}

	public CustomEmpDialogFragment() {

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		employeeDAO = new EvenementDAO(getActivity());

		Bundle bundle = this.getArguments();
		evenement = bundle.getParcelable("selectedEvenement");

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View customDialogView = inflater.inflate(R.layout.fragment_add_emp,
				null);
		builder.setView(customDialogView);

		//empNameEtxt = (EditText) customDialogView.findViewById(R.id.etxt_name);
		empNameEtxt = (Spinner) customDialogView.findViewById(R.id.etxt_name);
		empSalaryEtxt = (EditText) customDialogView.findViewById(R.id.etxt_salary);
		empDobEtxt = (EditText) customDialogView.findViewById(R.id.etxt_dob);
		deptSpinner = (Spinner) customDialogView.findViewById(R.id.spinner_dept);
		submitLayout = (LinearLayout) customDialogView.findViewById(R.id.layout_submit);
		submitLayout.setVisibility(View.GONE);
		setValue();

		builder.setTitle("Modifier Evenement");
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.update,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//evenement.setIdSymbole(Integer.parseInt(empNameEtxt.getText().toString()));
						Symboles symb = (Symboles) adapterSymb
								.getItem(empNameEtxt.getSelectedItemPosition());
						evenement.setSymboles(symb);
						evenement.setCouleur(empDobEtxt.getText().toString());
						evenement.setnTemps(Integer.parseInt(empSalaryEtxt.getText().toString()));
						//employee.setnTemps(Integer.parseInt(empSalaryEtxt.getText().toString()));

						Musique dept = (Musique) adapter
								.getItem(deptSpinner.getSelectedItemPosition());
						evenement.setMusique(dept);
						long result = employeeDAO.update(evenement);
						if (result > 0) {
							MainActivity activity = (MainActivity) getActivity();
							activity.onFinishDialog();
						} else {
							Toast.makeText(getActivity(),
									"Impossible de changer un evénement",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

					}
				});

		AlertDialog alertDialog = builder.create();

		return alertDialog;
	}

	private void setValue() {
		MusiqueDAO musiqueDAO = new MusiqueDAO(getActivity());
		SymbolesDAO symbolesDAO = new SymbolesDAO(getActivity());

		List<Musique> departments = musiqueDAO.getMusiques();
		List<Symboles> symboles = symbolesDAO.getSymboles();

		adapterSymb = new ArrayAdapter<Symboles>(getActivity(),
				android.R.layout.simple_list_item_1, symboles);
		adapter = new ArrayAdapter<Musique>(getActivity(),
				android.R.layout.simple_list_item_1, departments);
//simple_list_item_1 à voir les Symboles
		empNameEtxt.setAdapter(adapterSymb);
		deptSpinner.setAdapter(adapter);

		int posSymb = adapterSymb.getPosition(evenement.getSymboles());

		int pos = adapter.getPosition(evenement.getMusiquet());

		if (evenement != null) {
			empNameEtxt.setSelection(posSymb);
			//empDobEtxt.setText(formatter.format(employee.getDateOfBirth()));
			empSalaryEtxt.setText(evenement.getnTemps() + "");
			empDobEtxt.setText(evenement.getCouleur()+ "");
			deptSpinner.setSelection(pos);
		}
	}
}

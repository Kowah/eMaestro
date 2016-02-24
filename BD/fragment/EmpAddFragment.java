package fr.istic.univ_rennes1.diengadama.mabdemaestro.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fr.istic.univ_rennes1.diengadama.mabdemaestro.R;
import fr.istic.univ_rennes1.diengadama.mabdemaestro.db.EvenementDAO;
import fr.istic.univ_rennes1.diengadama.mabdemaestro.db.MusiqueDAO;
import fr.istic.univ_rennes1.diengadama.mabdemaestro.db.SymbolesDAO;
import fr.istic.univ_rennes1.diengadama.mabdemaestro.to.Evenement;
import fr.istic.univ_rennes1.diengadama.mabdemaestro.to.Musique;
import fr.istic.univ_rennes1.diengadama.mabdemaestro.to.Symboles;

public class EmpAddFragment extends Fragment implements OnClickListener {

	// UI references
	//private EditText empNameEtxt;
	private EditText empSalaryEtxt;//Couleur
	private EditText empDobEtxt;//Temps
	private Spinner deptSpinner;
	private Spinner empNameEtxt;
	private Button addButton;
	private Button resetButton;

	private static final SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.ENGLISH);

	DatePickerDialog datePickerDialog;
	Calendar dateCalendar;

	Evenement evenement = null;
	private EvenementDAO employeeDAO;
	private MusiqueDAO musiqueDAO;
	private SymbolesDAO symbolesDAO;
	private GetDeptTask task;
	private GetSymbTask task2;
	private AddEmpTask addEmpTask;

	public static final String ARG_ITEM_ID = "emp_add_fragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		employeeDAO = new EvenementDAO(getActivity());
		musiqueDAO = new MusiqueDAO(getActivity());
		symbolesDAO = new SymbolesDAO(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_add_emp, container,
				false);

		findViewsById(rootView);

		setListeners();

		// Used for orientation change
		/*
		 * After entering the fields, change the orientation.
		 * NullPointerException occurs for date. This piece of code avoids it.
		 */
		/*if (savedInstanceState != null) {
			dateCalendar = Calendar.getInstance();
			if (savedInstanceState.getLong("dateCalendar") != 0)
				dateCalendar.setTime(new Date(savedInstanceState
						.getLong("dateCalendar")));
		}*/

		// asynchronously retrieves Musique from table and sets it in Spinner
		task = new GetDeptTask(getActivity());
		task.execute((Void) null);

		// asynchronously retrieves Musique from table and sets it in Spinner
		task2 = new GetSymbTask(getActivity());
		task2.execute((Void) null);

		return rootView;
	}

	private void setListeners() {
		//empDobEtxt.setOnClickListener(this);
		Calendar newCalendar = Calendar.getInstance();
		datePickerDialog = new DatePickerDialog(getActivity(),
				new OnDateSetListener() {

					public void onDateSet(DatePicker view, int year,
										  int monthOfYear, int dayOfMonth) {
						dateCalendar = Calendar.getInstance();
						dateCalendar.set(year, monthOfYear, dayOfMonth);
						//empDobEtxt.setText(formatter.format(dateCalendar.getTime()));
					}

				}, newCalendar.get(Calendar.YEAR),
				newCalendar.get(Calendar.MONTH),
				newCalendar.get(Calendar.DAY_OF_MONTH));

		addButton.setOnClickListener(this);
		resetButton.setOnClickListener(this);
	}

	protected void resetAllFields() {
		//empNameEtxt.setText("");//idSymb
		if (empNameEtxt.getAdapter().getCount() > 0)
			empNameEtxt.setSelection(0);
		empDobEtxt.setText("");//couleur
		empSalaryEtxt.setText("");//nTemps
		if (deptSpinner.getAdapter().getCount() > 0)
			deptSpinner.setSelection(0);
	}

	private void setEmployee() {
		evenement = new Evenement();
		//employee.setName(empNameEtxt.getText().toString());
		//evenement.setIdSymbole(Integer.parseInt(empNameEtxt.getText().toString()));
		Symboles selectedSymb = (Symboles) empNameEtxt.getSelectedItem();
		evenement.setSymboles(selectedSymb);
		/*employee.setSalary(Double.parseDouble(empSalaryEtxt.getText()
				.toString()));*/
		evenement.setCouleur(empDobEtxt.getText().toString());
		evenement.setnTemps(Integer.parseInt(empSalaryEtxt.getText().toString()));
		//if (dateCalendar != null)
		//employee.setDateOfBirth(dateCalendar.getTime().toString());
		//employee.setDateOfBirth(empDobEtxt.getText().toString());
		Musique selectedDept = (Musique) deptSpinner.getSelectedItem();
		evenement.setMusique(selectedDept);
	}

	@Override
	public void onResume() {
		getActivity().setTitle(R.string.add_emp);
		getActivity().getActionBar().setTitle(R.string.add_emp);
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (dateCalendar != null)
			outState.putLong("dateCalendar", dateCalendar.getTime().getTime());
	}

	private void findViewsById(View rootView) {
		//empNameEtxt = (EditText) rootView.findViewById(R.id.etxt_name);
		empNameEtxt = (Spinner) rootView.findViewById(R.id.etxt_name);
		empDobEtxt = (EditText) rootView.findViewById(R.id.etxt_dob);
		empSalaryEtxt = (EditText) rootView.findViewById(R.id.etxt_salary);
		//	empDobEtxt.setInputType(InputType.TYPE_NULL);
	//	empDobEtxt = (EditText) rootView.findViewById(R.id.etxt_dob);

		deptSpinner = (Spinner) rootView.findViewById(R.id.spinner_dept);
		addButton = (Button) rootView.findViewById(R.id.button_add);
		resetButton = (Button) rootView.findViewById(R.id.button_reset);
	}

	@Override
	public void onClick(View view) {
		//if (view == empDobEtxt) {
		//	datePickerDialog.show();
		//} else
		if (view == addButton) {
			setEmployee();
			addEmpTask = new AddEmpTask(getActivity());
			addEmpTask.execute((Void) null);
		} else if (view == resetButton) {
			resetAllFields();
		}
	}

	public class GetDeptTask extends AsyncTask<Void, Void, Void> {

		private final WeakReference<Activity> activityWeakRef;
		private List<Musique> musiques;

		public GetDeptTask(Activity context) {
			this.activityWeakRef = new WeakReference<Activity>(context);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			musiques = musiqueDAO.getMusiques();
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			if (activityWeakRef.get() != null
					&& !activityWeakRef.get().isFinishing()) {

				ArrayAdapter<Musique> adapter = new ArrayAdapter<Musique>(
						activityWeakRef.get(),
						android.R.layout.simple_list_item_1, musiques);
				deptSpinner.setAdapter(adapter);

				addButton.setEnabled(true);
			}
		}
	}
//Class getSymbTask

	public class GetSymbTask extends AsyncTask<Void, Void, Void> {

		private final WeakReference<Activity> activityWeakRef;
		private List<Symboles> symboles;

		public GetSymbTask(Activity context) {
			this.activityWeakRef = new WeakReference<Activity>(context);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			symboles = symbolesDAO.getSymboles();
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			if (activityWeakRef.get() != null
					&& !activityWeakRef.get().isFinishing()) {

				ArrayAdapter<Symboles> adapter = new ArrayAdapter<Symboles>(
						activityWeakRef.get(),
						android.R.layout.simple_list_item_1, symboles);
				empNameEtxt.setAdapter(adapter);

				addButton.setEnabled(true);
			}
		}
	}
	//Fin
	public class AddEmpTask extends AsyncTask<Void, Void, Long> {

		private final WeakReference<Activity> activityWeakRef;

		public AddEmpTask(Activity context) {
			this.activityWeakRef = new WeakReference<Activity>(context);
		}

		@Override
		protected Long doInBackground(Void... arg0) {
			long result = employeeDAO.save(evenement);
			return result;
		}

		@Override
		protected void onPostExecute(Long result) {
			if (activityWeakRef.get() != null
					&& !activityWeakRef.get().isFinishing()) {
				if (result != -1)
					Toast.makeText(activityWeakRef.get(), "Evénement Sauvegardé !",
							Toast.LENGTH_LONG).show();
			}
		}
	}
}

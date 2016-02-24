package fr.istic.univ_rennes1.diengadama.mabdemaestro.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import fr.istic.univ_rennes1.diengadama.mabdemaestro.R;
import fr.istic.univ_rennes1.diengadama.mabdemaestro.adapter.EvenListAdapter;
import fr.istic.univ_rennes1.diengadama.mabdemaestro.db.EvenementDAO;
import fr.istic.univ_rennes1.diengadama.mabdemaestro.to.Evenement;

public class EmpListFragment extends Fragment implements OnItemClickListener,
		OnItemLongClickListener {

	public static final String ARG_ITEM_ID = "employee_list";

	Activity activity;
	ListView evenementListView;
	ArrayList<Evenement> evenements;

	EvenListAdapter evenementListAdapter;
	EvenementDAO evenementDAO;

	private GetEmpTask task;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
		evenementDAO = new EvenementDAO(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_emp_list, container,
				false);
		findViewsById(view);

		task = new GetEmpTask(activity);
		task.execute((Void) null);

		evenementListView.setOnItemClickListener(this);
		evenementListView.setOnItemLongClickListener(this);
		return view;
	}

	private void findViewsById(View view) {
		evenementListView = (ListView) view.findViewById(R.id.list_emp);
	}

	@Override
	public void onItemClick(AdapterView<?> list, View view, int position,
							long id) {
		Evenement evenement = (Evenement) list.getItemAtPosition(position);

		if (evenement != null) {
			Bundle arguments = new Bundle();
			arguments.putParcelable("selectedEvenement", evenement);
			CustomEmpDialogFragment customEmpDialogFragment = new CustomEmpDialogFragment();
			customEmpDialogFragment.setArguments(arguments);
			customEmpDialogFragment.show(getFragmentManager(),
					CustomEmpDialogFragment.ARG_ITEM_ID);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
								   int position, long id) {
		Evenement evenement = (Evenement) parent.getItemAtPosition(position);
		// Use AsyncTask to delete from database
		evenementDAO.deleteEvenement(evenement);
		evenementListAdapter.remove(evenement);

		return true;
	}

	public class GetEmpTask extends AsyncTask<Void, Void, ArrayList<Evenement>> {

		private final WeakReference<Activity> activityWeakRef;

		public GetEmpTask(Activity context) {
			this.activityWeakRef = new WeakReference<Activity>(context);
		}

		@Override
		protected ArrayList<Evenement> doInBackground(Void... arg0) {
			ArrayList<Evenement> evenementList = evenementDAO.getEvenements();
			return evenementList;
		}

		@Override
		protected void onPostExecute(ArrayList<Evenement> empList) {
			if (activityWeakRef.get() != null
					&& !activityWeakRef.get().isFinishing()) {
				evenements = empList;
				if (empList != null) {
					if (empList.size() != 0) {
						evenementListAdapter = new EvenListAdapter(activity,
								empList);
						evenementListView.setAdapter(evenementListAdapter);
					} else {
						Toast.makeText(activity, "Aucun Evénement",
								Toast.LENGTH_LONG).show();
					}
				}
			}
		}
	}

	/*
	 * This method is invoked from MainActivity onFinishDialog() method. It is
	 * called from CustomEmpDialogFragment when an employee record is updated.
	 * This is used for communicating between fragments.
	 */
	public void updateView() {
		task = new GetEmpTask(activity);
		task.execute((Void) null);
	}

	@Override
	public void onResume() {
		getActivity().setTitle(R.string.app_name);
		getActivity().getActionBar().setTitle(R.string.app_name);
		super.onResume();
	}
}

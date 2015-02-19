package logofhealth.com.logofhealth.exercise;

/**
 * Created by Kenny on 2/16/2015.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.software.shell.fab.ActionButton;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import logofhealth.com.logofhealth.R;
import logofhealth.com.logofhealth.adapter.MySQLiteHelper;
import logofhealth.com.logofhealth.dao.ExerciseDAO;
import logofhealth.com.logofhealth.extra.WeightFragment;

public class Exercises extends Fragment {

    MySQLiteHelper db;
    ListView lv;
    List<ExerciseDAO> data;
    ArrayAdapter adapter;
    ExerciseDAO e = new ExerciseDAO();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.exercise_fragment, container, false);
        db = new MySQLiteHelper(getActivity());
        ActionButton actionButton = (ActionButton) v.findViewById(R.id.action_button);
        actionButton.show();
        data = db.getAllExercise();
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, data);
        lv = (ListView) v.findViewById(R.id.listView);
        lv.setAdapter(adapter);
        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                String options[] = {"Open", "Add to Program", "Delete"};

                final AlertDialog.Builder alertDialogMain = new AlertDialog.Builder(getActivity());
                alertDialogMain.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Bundle bundle = new Bundle();
                                bundle.putString("Title", data.get(position).getTitle().toString());
                                Intent i = new Intent(getActivity(), WeightFragment.class);
                                i.putExtras(bundle);
                                startActivity(i);

                                break;
                            case 1:
                                db.addProgram(data.get(position));
                                db.getAllMeal();
                                adapter.notifyDataSetChanged();
                                SweetAlertDialog sad = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                                sad.setTitleText("Success")
                                        .setContentText(data.get(position).getTitle() + " has been added").show();

                                break;
                            case 2:
                                final SweetAlertDialog alertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                                alertDialog.setTitleText("Delete " + data.get(position).getTitle() + "?")
                                        .setContentText("Are you sure you want to delete " + data.get(position).getTitle() + " ?")
                                        .setConfirmText("Yes")
                                        .setCancelText("No")
                                        .showCancelButton(true)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.setTitleText("Deleted!")
                                                        .setContentText(data.get(position).getTitle() + " has been deleted")
                                                        .setConfirmText("Ok")
                                                        .showCancelButton(false)
                                                        .setConfirmClickListener(null)
                                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                                db.deleteExercise(data.get(position).getExerciseID());
                                                data.remove(position);
                                                adapter.notifyDataSetChanged();
                                            }
                                        })
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
                                                Crouton.makeText(getActivity(), data.get(position).getTitle() + " was not deleted", Style.INFO)
                                                        .setConfiguration(new Configuration.Builder().setDuration(1000).build()).show();
                                            }
                                        });
                                alertDialog.show();
                                break;
                        }
                    }
                });
                alertDialogMain.create().show();
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Add Exercise");
                final EditText exercise = new EditText(getActivity());
                alertDialog.setView(exercise)
                        .setCancelable(true)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String exerciseName = exercise.getText().toString();
                                db.addExercise(new ExerciseDAO(exerciseName));
                                data.add(new ExerciseDAO(exerciseName));
                                Crouton.makeText(getActivity(), exerciseName + " has been added", Style.CONFIRM).setConfiguration(new Configuration.Builder().setDuration(700).build()).show();

                            }
                        })
                        .show();

            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.exercise_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_exercise).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                FilterExercise();
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_exercise:

                return true;

            case R.id.delete_exercise:
                final SweetAlertDialog exerciseDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                exerciseDialog.setTitleText("Delete exercises?")
                        .setContentText("Are you sure you want to delete all exercises?")
                        .setConfirmText("Yes")
                        .setCancelText("No")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.setTitleText("Deleted!")
                                        .setContentText("Exercises has been deleted")
                                        .setConfirmText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                db.deleteAllExercise();
                                data.clear();
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                Crouton.makeText(getActivity(), "Exercises was not deleted", Style.INFO)
                                        .setConfiguration(new de.keyboardsurfer.android.widget.crouton.Configuration.Builder().setDuration(1000).build()).show();
                            }
                        });
                exerciseDialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void FilterExercise() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                String options[] = {"Open", "Add to Program", "Delete"};

                final AlertDialog.Builder alertDialogMain = new AlertDialog.Builder(getActivity());
                alertDialogMain.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Bundle bundle = new Bundle();
                                bundle.putString("Title", parent.getAdapter().getItem(position).toString());
                                Intent i = new Intent(getActivity(), WeightFragment.class);
                                i.putExtras(bundle);
                                startActivity(i);

                                break;
                            case 1:
                                db.addProgram(data.get(position));
                                db.getAllMeal();
                                adapter.notifyDataSetChanged();
                                SweetAlertDialog sad = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                                sad.setTitleText("Success")
                                        .setContentText(data.get(position).getTitle() + " has been added").show();

                                break;
                            case 2:
                                final SweetAlertDialog alertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                                alertDialog.setTitleText("Delete " + data.get(position).getTitle() + "?")
                                        .setContentText("Are you sure you want to delete " + data.get(position).getTitle() + " ?")
                                        .setConfirmText("Yes")
                                        .setCancelText("No")
                                        .showCancelButton(true)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.setTitleText("Deleted!")
                                                        .setContentText(data.get(position).getTitle() + " has been deleted")
                                                        .setConfirmText("Ok")
                                                        .showCancelButton(false)
                                                        .setConfirmClickListener(null)
                                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                                db.deleteExercise(data.get(position).getExerciseID());
                                                data.remove(position);
                                                adapter.notifyDataSetChanged();
                                            }
                                        })
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
                                                Crouton.makeText(getActivity(), data.get(position).getTitle() + " was not deleted", Style.INFO)
                                                        .setConfiguration(new Configuration.Builder().setDuration(1000).build()).show();
                                            }
                                        });
                                alertDialog.show();
                                break;
                        }
                    }
                });
                alertDialogMain.create().show();
            }
        });
    }
}

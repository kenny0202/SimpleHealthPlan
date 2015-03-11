package logofhealth.com.kenny.exercise;

/**
 * Created by Kenny on 2/16/2015.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.software.shell.fab.ActionButton;

import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import logofhealth.com.kenny.R;
import logofhealth.com.kenny.adapter.MySQLiteHelper;
import logofhealth.com.kenny.adapter.Validation;
import logofhealth.com.kenny.dao.ExerciseDAO;

public class Exercises extends Fragment {

    MySQLiteHelper db;
    ListView lv;
    List<ExerciseDAO> data;
    ArrayAdapter adapter;
    EditText exercise;

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
                String options[] = {"Schedule", "Add to Program", "Delete"};
                MaterialDialog.Builder mdialog = new MaterialDialog.Builder(getActivity());
                mdialog.items(options)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog materialDialog, View view, int which, CharSequence charSequence) {
                                switch (which) {
                                    case 0:
                                        Calendar c = Calendar.getInstance();
                                        Intent intent = new Intent(Intent.ACTION_EDIT);
                                        intent.setType("vnd.android.cursor.item/event");
                                        intent.putExtra("beginTime", c.getTimeInMillis());
                                        intent.putExtra("endTime", c.getTimeInMillis());
                                        intent.putExtra("allDay", true);
                                        intent.putExtra("title", data.get(position).getTitle());
                                        startActivity(intent);

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
                mdialog.show();
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Add Exercise");
                exercise = new EditText(getActivity());
                exercise.getBackground().setColorFilter(Color.rgb(37, 155, 36), PorterDuff.Mode.SRC_IN);
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
                                if (checkValidation()) {
                                    String exerciseName = exercise.getText().toString();
                                    db.addExercise(new ExerciseDAO(exerciseName));
                                    data.add(new ExerciseDAO(exerciseName));
                                    Crouton.makeText(getActivity(), exerciseName + " has been added.", Style.CONFIRM).setConfiguration(new Configuration.Builder().setDuration(700).build()).show();
                                } else {
                                    Crouton.makeText(getActivity(), "Missing title", Style.ALERT).setConfiguration(new Configuration.Builder().setDuration(1000).build()).show();
                                }
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
                String options[] = {"Schedule", "Add to Program", "Delete"};
                MaterialDialog.Builder mdialog = new MaterialDialog.Builder(getActivity());
                mdialog.items(options)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog materialDialog, View view, int which, CharSequence charSequence) {
                                switch (which) {
                                    case 0:
                                        Calendar c = Calendar.getInstance();
                                        Intent intent = new Intent(Intent.ACTION_EDIT);
                                        intent.setType("vnd.android.cursor.item/event");
                                        intent.putExtra("beginTime", c.getTimeInMillis());
                                        intent.putExtra("endTime", c.getTimeInMillis());
                                        intent.putExtra("allDay", true);
                                        intent.putExtra("title", parent.getAdapter().getItem(position).toString());
                                        startActivity(intent);
                                        break;
                                    case 1:
                                        db.addProgram(((ExerciseDAO) parent.getAdapter().getItem(position)));
                                        db.getAllProgram();
                                        adapter.notifyDataSetChanged();
                                        SweetAlertDialog sad = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                                        sad.setTitleText("Success")
                                                .setContentText(((ExerciseDAO) parent.getAdapter().getItem(position)).getTitle().toString() + " has been added").show();
                                        break;
                                    case 2:
                                        final SweetAlertDialog alertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                                        alertDialog.setTitleText("Delete " + ((ExerciseDAO) parent.getAdapter().getItem(position)).getTitle().toString() + "?")
                                                .setContentText("Are you sure you want to delete " + ((ExerciseDAO) parent.getAdapter().getItem(position)).getTitle().toString() + " ?")
                                                .setConfirmText("Yes")
                                                .setCancelText("No")
                                                .showCancelButton(true)
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.setTitleText("Deleted!")
                                                                .setContentText(((ExerciseDAO) parent.getAdapter().getItem(position)).getTitle().toString() + " has been deleted")
                                                                .setConfirmText("Ok")
                                                                .showCancelButton(false)
                                                                .setConfirmClickListener(null)
                                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                                        db.deleteExercise(data.get(position).getExerciseID());
                                                        db.deleteExercise((((ExerciseDAO) parent.getAdapter().getItem(position)).getExerciseID()));
                                                        data.remove(position);
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                })
                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.dismissWithAnimation();
                                                        Crouton.makeText(getActivity(), parent.getAdapter().getItem(position).toString() + " was not deleted", Style.INFO)
                                                                .setConfiguration(new Configuration.Builder().setDuration(1000).build()).show();
                                                    }
                                                });
                                        alertDialog.show();
                                        break;
                                }
                            }
                        });
                mdialog.show();
            }
        });
    }

    //validation method
    private boolean checkValidation() {
        boolean value = true;

        if (!Validation.hasText(exercise))
            value = false;
        return value;
    }
}

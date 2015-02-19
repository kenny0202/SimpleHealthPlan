package logofhealth.com.logofhealth.exercise;

/**
 * Created by Kenny on 2/16/2015.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mobeta.android.dslv.DragSortListView;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import logofhealth.com.logofhealth.R;
import logofhealth.com.logofhealth.adapter.MySQLiteHelper;
import logofhealth.com.logofhealth.dao.ExerciseDAO;

public class Programs extends Fragment {

    ArrayAdapter<ExerciseDAO> adapter;
    MySQLiteHelper db;
    DragSortListView lv;
    List<ExerciseDAO> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.program_fragment, container, false);
        lv = (DragSortListView) v.findViewById(R.id.listView2);
        db = new MySQLiteHelper(getActivity());
        data = db.getAllProgram();
        adapter = new ArrayAdapter<ExerciseDAO>(getActivity(), android.R.layout.simple_list_item_1, data);
        lv.setAdapter(adapter);
        lv.setDropListener(new DragSortListView.DropListener() {
            @Override
            public void drop(int from, int to) {
                if (from != to) {
                    ExerciseDAO item = adapter.getItem(from);
                    adapter.remove(item);
                    adapter.insert(item, to);
                    db.updateProgram(data);
                    adapter.notifyDataSetChanged();
                }
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
        inflater.inflate(R.menu.program_menu, menu);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.delete_program:
                final SweetAlertDialog mealDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                mealDialog.setTitleText("Delete Program?")
                        .setContentText("Are you sure you want to delete your current program?")
                        .setConfirmText("Yes")
                        .setCancelText("No")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.setTitleText("Deleted!")
                                        .setContentText("Program has been deleted")
                                        .setConfirmText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                db.deleteAllProgram();
                                data.clear();
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                Crouton.makeText(getActivity(), "Current program was not deleted", Style.INFO)
                                        .setConfiguration(new de.keyboardsurfer.android.widget.crouton.Configuration.Builder().setDuration(1000).build()).show();
                            }
                        });
                mealDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
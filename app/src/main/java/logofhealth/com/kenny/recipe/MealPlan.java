package logofhealth.com.kenny.recipe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.mobeta.android.dslv.DragSortListView;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import logofhealth.com.kenny.R;
import logofhealth.com.kenny.adapter.MySQLiteHelper;
import logofhealth.com.kenny.dao.RecipeDAO;

/**
 * Created by Kenny on 2/16/2015.
 */
public class MealPlan extends Fragment {

    ArrayAdapter<RecipeDAO> adapter;
    MySQLiteHelper db;
    DragSortListView lv;
    List<RecipeDAO> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mealplan_fragment, container, false);

        lv = (DragSortListView) v.findViewById(R.id.listView2);

        db = new MySQLiteHelper(getActivity());
        data = db.getAllMeal();
        adapter = new ArrayAdapter<RecipeDAO>(getActivity(), android.R.layout.simple_list_item_1, data);
        lv.setAdapter(adapter);


        lv.setDropListener(new DragSortListView.DropListener() {
            @Override
            public void drop(int from, int to) {
                if (from != to) {
                    RecipeDAO item = adapter.getItem(from);
                    adapter.remove(item);
                    adapter.insert(item, to);
                    db.updateMealList(data);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //db.deleteRecipe(data.get(position).getRecipeID());
                Crouton.makeText(getActivity(), " deleted", Style.CONFIRM).setConfiguration(new Configuration.Builder().setDuration(700).build()).show();
                Toast.makeText(getActivity(), " deleted", Toast.LENGTH_SHORT).show();
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
        inflater.inflate(R.menu.meal_menu, menu);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_meal:
                final SweetAlertDialog mealDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                mealDialog.setTitleText("Delete Meal plan?")
                        .setContentText("Are you sure you want to delete your current meal plan?")
                        .setConfirmText("Yes")
                        .setCancelText("No")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.setTitleText("Deleted!")
                                        .setContentText("Meal plan has been deleted")
                                        .setConfirmText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                db.deleteAllMeal();
                                data.clear();
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                Crouton.makeText(getActivity(), "Current meal plan was not deleted", Style.INFO)
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

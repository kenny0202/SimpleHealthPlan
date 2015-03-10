package logofhealth.com.kenny.recipe;

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
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import logofhealth.com.kenny.R;
import logofhealth.com.kenny.adapter.MySQLiteHelper;
import logofhealth.com.kenny.dao.RecipeDAO;
import logofhealth.com.kenny.extra.DisplayRecipeDetail;

/**
 * Created by Kenny on 2/16/2015.
 */
public class Recipes extends Fragment {

    MySQLiteHelper db;
    ListView lv;
    List<RecipeDAO> data;
    ArrayAdapter adapter;

    public Recipes() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.recipe_fragment, container, false);

        db = new MySQLiteHelper(getActivity());
        data = db.getAllRecipe();
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, data);
        lv = (ListView) v.findViewById(R.id.listView2);
        lv.setAdapter(adapter);
        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String options[] = {"Open", "Add to Meal", "Delete"};
                MaterialDialog.Builder mdialog = new MaterialDialog.Builder(getActivity());
                mdialog.items(options)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog materialDialog, View view, int which, CharSequence charSequence) {
                                switch (which) {
                                    case 0:
                                        Bundle bundle = new Bundle();
                                        bundle.putString("Title", data.get(position).getTitle().toString());
                                        bundle.putString("Ingredients", data.get(position).getIngredients().toString());
                                        bundle.putString("Instructions", data.get(position).getInstructions().toString());
                                        Intent i = new Intent(getActivity(), DisplayRecipeDetail.class);
                                        i.putExtras(bundle);
                                        startActivity(i);

                                        break;
                                    case 1:
                                        db.addMeal(data.get(position));
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

                                                        db.deleteRecipe(data.get(position).getRecipeID());
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

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.recipe_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_recipe).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                FilterRecipe();
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_recipe:
                final SweetAlertDialog recipeDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                recipeDialog.setTitleText("Delete recipes?")
                        .setContentText("Are you sure you want to delete all recipes?")
                        .setConfirmText("Yes")
                        .setCancelText("No")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.setTitleText("Deleted!")
                                        .setContentText("Recipes has been deleted")
                                        .setConfirmText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                db.deleteAllRecipe();
                                data.removeAll(data);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                Crouton.makeText(getActivity(), "Recipes was not deleted", Style.INFO)
                                        .setConfiguration(new de.keyboardsurfer.android.widget.crouton.Configuration.Builder().setDuration(1000).build()).show();
                            }
                        });
                recipeDialog.show();
                return true;

            case R.id.search_recipe:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void FilterRecipe() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                String options[] = {"Open", "Add to Meal", "Delete"};
                MaterialDialog.Builder mdialog = new MaterialDialog.Builder(getActivity());
                mdialog.items(options)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog materialDialog, View view, int which, CharSequence charSequence) {
                                switch (which) {
                                    case 0:
                                        Bundle bundle = new Bundle();
                                        bundle.putString("Title", parent.getAdapter().getItem(position).toString());
                                        bundle.putString("Ingredients", ((RecipeDAO) parent.getAdapter().getItem(position)).getIngredients().toString());
                                        bundle.putString("Instructions", ((RecipeDAO) parent.getAdapter().getItem(position)).getInstructions().toString());
                                        Intent i = new Intent(getActivity(), DisplayRecipeDetail.class);
                                        i.putExtras(bundle);
                                        startActivity(i);

                                        break;
                                    case 1:
                                        db.addMeal((RecipeDAO) parent.getAdapter().getItem(position));
                                        db.getAllMeal();
                                        adapter.notifyDataSetChanged();
                                        SweetAlertDialog sad = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                                        sad.setTitleText("Success")
                                                .setContentText((((RecipeDAO) parent.getAdapter().getItem(position)).getTitle()) + " has been added").show();

                                        break;
                                    case 2:
                                        final SweetAlertDialog alertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                                        alertDialog.setTitleText("Delete " + ((RecipeDAO) parent.getAdapter().getItem(position)).getTitle().toString() + "?")
                                                .setContentText("Are you sure you want to delete " + ((RecipeDAO) parent.getAdapter().getItem(position)).getTitle().toString() + " ?")
                                                .setConfirmText("Yes")
                                                .setCancelText("No")
                                                .showCancelButton(true)
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.setTitleText("Deleted!")
                                                                .setContentText(((RecipeDAO) parent.getAdapter().getItem(position)).getTitle().toString() + " has been deleted")
                                                                .setConfirmText("Ok")
                                                                .showCancelButton(false)
                                                                .setConfirmClickListener(null)
                                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                                        db.deleteRecipe((((RecipeDAO) parent.getAdapter().getItem(position)).getRecipeID()));
                                                        data.remove(position);
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                })
                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.dismissWithAnimation();
                                                        Crouton.makeText(getActivity(), ((RecipeDAO) parent.getAdapter().getItem(position)).getTitle().toString() + " was not deleted", Style.INFO)
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
}
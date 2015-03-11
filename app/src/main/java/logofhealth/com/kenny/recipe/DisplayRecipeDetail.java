package logofhealth.com.kenny.recipe;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import logofhealth.com.kenny.R;


/**
 * Created by Kenny on 2/16/2015.
 */
public class DisplayRecipeDetail extends ActionBarActivity {
    private Toolbar toolbar;
    private TextView ingredients, instructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_recipe);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ingredients = (TextView) findViewById(R.id.ingredients);
        instructions = (TextView) findViewById(R.id.instructions);

        Bundle bundle = getIntent().getExtras();
        final String title = bundle.getString("Title").toUpperCase();
        String ingred = bundle.getString("Ingredients");
        String instrut = bundle.getString("Instructions");

        getSupportActionBar().setTitle(title);
        ingredients.setText(ingred);
        instructions.setText(instrut);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}

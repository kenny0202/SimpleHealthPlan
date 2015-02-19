package logofhealth.com.logofhealth.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import logofhealth.com.logofhealth.R;

public class MainMenu extends Activity implements View.OnClickListener {

    Button recipeButton, exerciseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        recipeButton = (Button) findViewById(R.id.recipe_button);
        exerciseButton = (Button) findViewById(R.id.exercise_button);

        recipeButton.setOnClickListener(this);
        exerciseButton.setOnClickListener(this);


    }

    public void onClick(View view) {
        if (view.getId() == R.id.recipe_button) {
            Intent recipeIntent = new Intent(this, RecipeMenu.class);
            startActivity(recipeIntent);
        } else if (view.getId() == R.id.exercise_button) {
            Intent exerciseIntent = new Intent(this, ExerciseMenu.class);
            startActivity(exerciseIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
package logofhealth.com.kenny.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import logofhealth.com.kenny.R;

public class MainMenu extends ActionBarActivity implements View.OnClickListener {

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
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
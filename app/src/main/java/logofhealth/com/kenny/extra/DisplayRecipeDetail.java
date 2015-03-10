package logofhealth.com.kenny.extra;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.software.shell.fab.ActionButton;

import logofhealth.com.kenny.R;


/**
 * Created by Kenny on 2/16/2015.
 */
public class DisplayRecipeDetail extends Activity {

    private TextView titleBar, ingredients, instructions;
    private ActionButton editButton, saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_recipe);

        titleBar = (TextView) findViewById(R.id.title);
        ingredients = (TextView) findViewById(R.id.ingredients);
        instructions = (TextView) findViewById(R.id.instructions);

        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("Title").toUpperCase();
        String ingred = bundle.getString("Ingredients");
        String instrut = bundle.getString("Instructions");

        titleBar.setText(title);
        ingredients.setText(ingred);
        instructions.setText(instrut);

        editButton = (ActionButton) findViewById(R.id.edit_button);
        saveButton = (ActionButton) findViewById(R.id.save_button);

        editButton.show();
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editButton.hide();
                saveButton.show();
                Toast.makeText(getApplication(), "Edit", Toast.LENGTH_SHORT).show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton.hide();
                editButton.show();
                Toast.makeText(getApplication(), "Save", Toast.LENGTH_SHORT).show();
            }
        });
    }
}



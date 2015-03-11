package logofhealth.com.kenny.recipe;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import logofhealth.com.kenny.R;


/**
 * Created by Kenny on 2/16/2015.
 */
public class DisplayRecipeDetail extends Activity {

    private TextView titleBar, ingredients, instructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_recipe);

        titleBar = (TextView) findViewById(R.id.title);
        ingredients = (TextView) findViewById(R.id.ingredients);
        instructions = (TextView) findViewById(R.id.instructions);

        Bundle bundle = getIntent().getExtras();
        final String title = bundle.getString("Title").toUpperCase();
        String ingred = bundle.getString("Ingredients");
        String instrut = bundle.getString("Instructions");

        titleBar.setText(title);
        ingredients.setText(ingred);
        instructions.setText(instrut);

    }
}

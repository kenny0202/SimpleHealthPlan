package logofhealth.com.kenny.recipe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.software.shell.fab.ActionButton;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import logofhealth.com.kenny.R;
import logofhealth.com.kenny.adapter.MySQLiteHelper;
import logofhealth.com.kenny.adapter.Validation;
import logofhealth.com.kenny.dao.RecipeDAO;

/**
 * Created by Kenny on 2/16/2015.
 */
public class CreateRecipe extends Fragment {
    EditText title_text, ingredients_text, instructions_text;
    MySQLiteHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_recipe_fragment, container, false);

        title_text = (EditText) v.findViewById(R.id.title);
        ingredients_text = (EditText) v.findViewById(R.id.ingredients);
        instructions_text = (EditText) v.findViewById(R.id.instructions);
        ActionButton addrecipeButton = (ActionButton) v.findViewById(R.id.action_button);
        addrecipeButton.show();

        addrecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()) {
                    String getTitle = title_text.getText().toString();
                    String getIngred = ingredients_text.getText().toString();
                    String getInstruct = instructions_text.getText().toString();
                    db = new MySQLiteHelper(getActivity());

                    db.addRecipe(new RecipeDAO(getTitle, getIngred, getInstruct));
                    Crouton.makeText(getActivity(), getTitle + " has been added", Style.CONFIRM).setConfiguration(new Configuration.Builder().setDuration(700).build()).show();
                    title_text.setText("");
                    ingredients_text.setText("");
                    instructions_text.setText("");
                } else {
                    Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                    title_text.startAnimation(shake);
                    title_text.setError("Missing Title");
                    Crouton.makeText(getActivity(), "Missing title", Style.ALERT).setConfiguration(new Configuration.Builder().setDuration(1000).build()).show();
                    title_text.requestFocus();
                }
            }
        });
        return v;
    }

    private boolean checkValidation() {
        boolean value = true;

        if (!Validation.hasText(title_text))
            value = false;
        return value;
    }
}
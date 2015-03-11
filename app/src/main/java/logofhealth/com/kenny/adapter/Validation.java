package logofhealth.com.kenny.adapter;

import android.widget.EditText;

/**
 * Created by Kenny on 2/16/2015.
 */
public class Validation {

    private static final String REQUIRED_MSG = "required";

    public static boolean isValid(EditText editText, String regex, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        editText.setError(null);
        if (required && !hasText(editText))
            return false;

        return true;
    }
    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        if (text.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }
        return true;
    }
}
package logofhealth.com.kenny.extra;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import logofhealth.com.kenny.R;

/**
 * Created by Kenny on 2/16/2015.
 */
public class WeightFragment extends Activity {

    private EditText reps, weight, date;
    private TextView titleBar, repsText, weightText, exercise;
    private ImageButton calendarButton;
    private Button save;
    private int mYear, mMonth, mDay;
    private DatePickerDialog datePicker;
    private SimpleDateFormat dateFormatter;

    public WeightFragment() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.weight_fragment);
        titleBar = (TextView) findViewById(R.id.title);
        reps = (EditText) findViewById(R.id.repsNumber);
        weight = (EditText) findViewById(R.id.weightNumber);
        repsText = (TextView) findViewById(R.id.reps);
        weightText = (TextView) findViewById(R.id.weight);

        date = (EditText) findViewById(R.id.date);
        save = (Button) findViewById(R.id.saveButton);
        calendarButton = (ImageButton) findViewById(R.id.calendarButton);

        Bundle bundle = getIntent().getExtras();
        String exerciseName = bundle.getString("Title").toUpperCase();
        titleBar.setText(exerciseName);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        //imageButton on click listener for image press
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog dpd = new DatePickerDialog(WeightFragment.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                date.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();

            }
        });

        //edittext view on click listener for edittext press
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog dpd = new DatePickerDialog(WeightFragment.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                date.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();

            }
        });
    }
}
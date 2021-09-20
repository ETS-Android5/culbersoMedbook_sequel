package com.example.culberso_medbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class EditMedication extends AppCompatActivity {
    /*
    This class is the activity for adding and modifying an existing medicine.
     */
    Medication med;

    Spinner dose_unit;
    EditText med_name;
    EditText dose_amount;
    CalendarView date_started;
    EditText daily_frequency;

    long date_selected = Calendar.getInstance().getTimeInMillis();



    public final List<String> dose_units = Arrays.asList("mg","mcg","drop");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
        initializing the EditMedication activity.
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medication);

        Intent intent = getIntent();

        dose_unit = findViewById(R.id.dose_unit);
        med_name = findViewById(R.id.med_name);
        dose_amount = findViewById(R.id.dose_amount);
        date_started = findViewById(R.id.date_started);
        daily_frequency = findViewById(R.id.med_freq);

        dose_unit.setAdapter(new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,dose_units));

        if(intent.hasExtra("medication")){
            med = (Medication) intent.getSerializableExtra("medication");
        }
        else{
            med = new Medication();
        }

        med_name.setText(med.get_name());
        dose_amount.setText(med.get_dose().toString());
        date_started.setDate(med.get_date_started());
        dose_unit.setSelection(dose_units.indexOf(med.get_dose_unit()));
        daily_frequency.setText(med.get_daily_frequency().toString());

        date_selected = med.get_date_started();

        ((CalendarView) findViewById(R.id.date_started)).setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                /*
                update calendar selection.
                 */
                GregorianCalendar cal = new GregorianCalendar(TimeZone.getDefault());
                cal.set(year,month,day);

                date_selected = cal.getTimeInMillis();
            }
        });

    }

    public void deleteMedication(View view){
        /*
        if the delete button is pressed, return to main activity specifying this.
         */
        Intent intent = new Intent();
        intent.putExtra("delete",true);
        setResult(RESULT_OK,intent);
        finish();
    }

    public void saveMedication(View view){
        /*
        extract all of the selected medication info and send this medication to MainActivity.
         */
        med.set_name(med_name.getText().toString());

        String d_f = daily_frequency.getText().toString();

        if(d_f.isEmpty()){
            med.set_daily_frequency(0);
        }
        else{
            med.set_daily_frequency(Integer.valueOf(d_f));
        }

        String d_a = dose_amount.getText().toString();

        if(d_a.isEmpty()){
            med.set_dose_amount(0);
        }
        else{
            med.set_dose_amount(Integer.valueOf(d_a));
        }

        med.set_date_started(date_selected);
        med.set_dose_unit(dose_unit.getSelectedItem().toString());


        Intent intent = new Intent();
        intent.putExtra("medication",(Serializable) med);

        setResult(RESULT_OK,intent);
        finish();
    }
}
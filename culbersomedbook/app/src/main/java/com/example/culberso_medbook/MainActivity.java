package com.example.culberso_medbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    /*
    This is the main activity for MedBook. Shows the list of all medicines,
    and a total list of all of the doses.
     */

    ListView med_list_view;
    ListView dose_total_view;

    ArrayAdapter<String> med_list_adapter;
    ArrayAdapter<String> dose_total_adapter;

    ArrayList<String> med_list;
    ArrayList<String> dose_total;

    ArrayList<Medication> med_info;


    int selected_medication = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
        initializing MedBook
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        med_list_view = findViewById(R.id.med_list);
        dose_total_view = findViewById(R.id.total_dose);

        med_list = new ArrayList<>();
        dose_total = new ArrayList<>();
        med_info = new ArrayList<>();

        updateListViews();

        med_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //When a medication is selected from a list, go to the edit medication activity
                selected_medication = i;
                Intent intent = new Intent(adapterView.getContext(),EditMedication.class);
                intent.putExtra("medication",med_info.get(selected_medication));

                startActivityForResult(intent,10);

            }
        });
    }

    private void updateDoseTotal(){
        /*
        This method computes the total dosage of each medication for the total dose list.
        Since we may take a medication with multiple different dose units, we first make a hashmap tying each
        medication to its different dose units, and then compute the resultant list to print.
         */
        HashMap<String,ArrayList<Integer>> dose_map = new HashMap<>();

        ArrayList<String> dose_units = new ArrayList<>();
        dose_units.add("mg");
        dose_units.add("mcg");
        dose_units.add("drop");

        for(Medication m : med_info){
            int i = dose_units.indexOf(m.get_dose_unit());
            if(dose_map.containsKey(m.get_name())){


                ArrayList<Integer> med_dose = dose_map.get(m.get_name());
                med_dose.set(i,med_dose.get(i) + m.get_dose()*m.get_daily_frequency());
                dose_map.put(m.get_name(),med_dose);
            }
            else{
                ArrayList<Integer> med_dose = new ArrayList<>();
                med_dose.add(0);
                med_dose.add(0);
                med_dose.add(0);
                med_dose.set(i,med_dose.get(i) + m.get_dose()*m.get_daily_frequency());
                dose_map.put(m.get_name(),med_dose);

            }
        }

        //Iterate through the hashmap to compute the string for each total dose entry
        ArrayList<String> res = new ArrayList<>();
        for(Map.Entry<String, ArrayList<Integer>> p : dose_map.entrySet()){
            String temp = p.getKey() + ": ";
            for(int i=0;i<p.getValue().size();i++){
                if(p.getValue().get(i) > 0){
                    temp += p.getValue().get(i) + " " + dose_units.get(i) + " ";
                }
            }

            res.add(temp);
        }

        //display the result
        dose_total_adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,res);
        dose_total_view.setAdapter(dose_total_adapter);
    }

    private void updateListViews(){
        /*
        update the two list views.
         */
        med_list_adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,med_list);
        med_list_view.setAdapter(med_list_adapter);

        dose_total_adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,dose_total);
        dose_total_view.setAdapter(dose_total_adapter);

        this.updateDoseTotal();
    }


    public void startEditMedication(View view){
        //When the add medication button is pressed change to the edit medication activity.

        Intent intent = new Intent(this,EditMedication.class);

        startActivityForResult(intent,10);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        /*
        This method gets called when the EditMedication activity returns. It gets the medication instance
        created by EditMedication and places it in its list, or if the delete method was called, it deletes the selected medicine.
         */
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("ADDING MEDICATION!");

        if(data.hasExtra("delete")){
            if(selected_medication == -1){
                return;
            }
            else{
                med_list.remove(selected_medication);
                med_info.remove(selected_medication);
            }
        }
        if(data.hasExtra("medication")){
            Medication med = (Medication) data.getExtras().getSerializable("medication");
            if(selected_medication != -1){
                med_info.set(selected_medication,med);
                med_list.set(selected_medication,med.toString());
            }
            else{
                med_info.add(med);
                med_list.add(med.toString());
            }
        }

        selected_medication = -1;
        updateListViews();
    }
}
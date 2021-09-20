package com.example.culberso_medbook;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Medication implements Serializable {
    /*
    This class represents an instance of a medication. It contains all of its relevant data, and methods to change its data.
    Made serializable so that it can be passed through Intent.
     */
    long date_started;
    String name;
    Integer dose_amount;
    String dose_unit;
    Integer daily_frequency;

    public Medication(){
        date_started = Calendar.getInstance().getTimeInMillis();
        name = "New Medication";
        dose_amount = 0;
        dose_unit = "mg";
        daily_frequency = 0;
    }

    @Override
    public String toString(){
        return this.name + " dose " + dose_amount + " " + dose_unit + ", frequency " + daily_frequency;
    }

    public void set_date_started(long date){
        date_started = date;
    }

    public void set_name(String name){
        this.name = name;
    }

    public void set_dose_amount(Integer dose){
        dose_amount = dose;
    }

    public void set_dose_unit(String dose){
        dose_unit = dose;
    }

    public void set_daily_frequency(Integer freq){
        daily_frequency = freq;
    }

    public String get_name(){
        return name;
    }

    public Integer get_dose(){
        return dose_amount;
    }

    public String get_dose_unit(){
        return dose_unit;
    }

    public Integer get_daily_frequency(){
        return daily_frequency;
    }

    public long get_date_started(){
        return date_started;
    }

}

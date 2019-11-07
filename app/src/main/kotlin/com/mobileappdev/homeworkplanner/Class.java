package com.mobileappdev.homeworkplanner;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class Class {
    private String mTitile;
    private ArrayList<String> mDaysOfWeek;
    private String mTimeOfDay;

    public String getTitile() {
        return mTitile;
    }

    public void setTitile(String titile) {
        mTitile = titile;
    }

    public ArrayList<String> getDaysOfWeek() { return mDaysOfWeek; }

    public void setDaysOfWeek(ArrayList<String> daysOfWeek){ mDaysOfWeek = daysOfWeek; }

    public String getTimeOfDay() { return mTimeOfDay; }

    public void setTimeOfDay(String timeOfDay) { mTimeOfDay = timeOfDay; }
}
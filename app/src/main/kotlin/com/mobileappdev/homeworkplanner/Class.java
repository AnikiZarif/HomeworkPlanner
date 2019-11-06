package com.mobileappdev.homeworkplanner;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.List;

public class Class {
    private String mTitile;
    private List<DayOfWeek> mDaysOfWeek;
    private Time mTimeOfDay;

    public String getTitile() {
        return mTitile;
    }

    public void setTitile(String titile) {
        mTitile = titile;
    }

    public List<DayOfWeek> getDaysOfWeek() { return mDaysOfWeek; }

    public void setDaysOfWeek(List<DayOfWeek> daysOfWeek){ mDaysOfWeek = daysOfWeek; }

    public Time getTimeOfDay() { return mTimeOfDay; }

    public void setTimeOfDay(Time timeOfDay) { mTimeOfDay = timeOfDay; }
}
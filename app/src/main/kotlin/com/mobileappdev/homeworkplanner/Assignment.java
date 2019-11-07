package com.mobileappdev.homeworkplanner;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;

public class Assignment {
    private String mName;
    private Date mDueDate;
    private Class mClass;
    private List<Date> mWorkPeriod;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Date getDueDate() {
        return mDueDate;
    }

    public void setDueDate(Date dueDate) {
        mDueDate = dueDate;
    }

    @NonNull
    @Override
    public Class getClass() {
        return mClass;
    }

    public void setClass(Class aClass) {
        mClass = aClass;
    }

    public List<Date> getWorkPeriod() {
        return mWorkPeriod;
    }

    public void setWorkPeriod(List<Date> workPeriod) {
        mWorkPeriod = workPeriod;
    }
}

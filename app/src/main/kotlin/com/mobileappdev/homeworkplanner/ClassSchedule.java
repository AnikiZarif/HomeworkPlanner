package com.mobileappdev.homeworkplanner;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ClassSchedule {
    private static ClassSchedule sClassSchedule;

    private List<Class> mClasses;

    public static ClassSchedule get(Context context){
        if (sClassSchedule == null){
            sClassSchedule = new ClassSchedule(context);
        }
        return sClassSchedule;
    }

    private ClassSchedule(Context context){
        mClasses = new ArrayList<>();

        // TODO: Populate with classes from database


        // Put dummy classes in there in the meantime
        for (int i = 0; i < 5; i++){
            Class aClass = new Class();
            aClass.setTitile("Class #" + i);
            mClasses.add(aClass);
        }
    }

    public List<Class> getClasses(){ return mClasses; }
}

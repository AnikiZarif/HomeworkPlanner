package com.mobileappdev.homeworkplanner;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AssignmentList {
    private static AssignmentList sAssignmentList;

    private List<Assignment> mAssignments;

    public static AssignmentList get(Context context){
        if (sAssignmentList == null){
            sAssignmentList = new AssignmentList(context);
        }
        return sAssignmentList;
    }

    private AssignmentList(Context context){
        mAssignments = new ArrayList<>();

        // TODO: Populate with all assignments from database


        // Put dummy classes in there in the meantime
        for (int i = 0; i < 5; i++){
            Assignment assignment = new Assignment();

            assignment.setParentClass(null);
            assignment.setName("Assignment #" + i);
            Calendar calendar = Calendar.getInstance();
            calendar.set(2019, 11, 6);
            Date date = calendar.getTime();
            assignment.setDueDate(date);
            List<Date> workPeriod = new ArrayList<>();
            calendar.set(2019, 11, 5);
            workPeriod.add(calendar.getTime());
            calendar.set(2019, 11, 4);
            workPeriod.add(calendar.getTime());
            assignment.setWorkPeriod(workPeriod);

            mAssignments.add(assignment);
        }
    }

    public List<Assignment> getAssignments(){ return mAssignments; }
}

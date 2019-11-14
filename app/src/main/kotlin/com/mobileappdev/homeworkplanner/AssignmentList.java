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
    }

    public List<Assignment> getAssignments(){ return mAssignments; }
}

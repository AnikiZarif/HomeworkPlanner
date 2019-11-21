package com.mobileappdev.homeworkplanner;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class AssignmentColorManager {
    private static int mCurrentColor;
    private static List<Integer> mWorkingColors;
    private static List<Integer> mDueColors;
    private static AssignmentColorManager mInstance;

    private AssignmentColorManager(){
        mWorkingColors = new ArrayList<>();
        mWorkingColors.add(R.color.working1);
        mWorkingColors.add(R.color.working2);
        mWorkingColors.add(R.color.working3);
        mWorkingColors.add(R.color.working4);
        mWorkingColors.add(R.color.working5);

        mDueColors = new ArrayList<>();
        mDueColors.add(R.color.due1);
        mDueColors.add(R.color.due2);
        mDueColors.add(R.color.due3);
        mDueColors.add(R.color.due4);
        mDueColors.add(R.color.due5);

        mCurrentColor = 0;
    }

    public static AssignmentColorManager getInstance(){
        if (mInstance == null){
            mInstance = new AssignmentColorManager();
        }

        return mInstance;
    }

    public List<Integer> getColors(){
        List<Integer> colors = new ArrayList<>();
        colors.add(mWorkingColors.get(mCurrentColor));
        colors.add(mDueColors.get(mCurrentColor));
        if (mCurrentColor == (mWorkingColors.size() - 1)){
            mCurrentColor = 0;
        } else {
            mCurrentColor += 1;
        }
        return colors;
    }
}

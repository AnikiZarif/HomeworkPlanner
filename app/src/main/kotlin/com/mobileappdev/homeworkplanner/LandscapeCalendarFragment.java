package com.mobileappdev.homeworkplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Calendar;

public class LandscapeCalendarFragment extends Fragment {

    private RecyclerView mAssignmentRecyclerView;
    private AssignmentAdapter mDueAssignmentAdapter;
    private TextView mCurrentWeekText;
    private Button mNextWeekButton;
    private Button mPrevWeekButton;

    private Date mCurrentDate = null;
    private Date mFirstDateOfWeek = null;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM/dd/yyyy");


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_landscape_calendar, container, false);

        // Get today's date
        if (mCurrentDate == null){
            Date today = new Date();
            mCurrentDate = today;
        }

        if (mFirstDateOfWeek == null){
            Calendar c = Calendar.getInstance();
            c.setTime(mCurrentDate);
            c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
            mFirstDateOfWeek = c.getTime();
        }

        mAssignmentRecyclerView = view.findViewById(R.id.assignment_recycler_view);
        mAssignmentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mCurrentWeekText = view.findViewById(R.id.landscape_view_week);

        // Set prev date
        mPrevWeekButton = view.findViewById(R.id.landscape_prev_button);
        mPrevWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                c.setTime(mFirstDateOfWeek);
                c.add(Calendar.WEEK_OF_YEAR, -1);
                mCurrentDate = c.getTime();
                mFirstDateOfWeek = c.getTime();
                updateUI();
            }
        });

        // Set next date
        mNextWeekButton = view.findViewById(R.id.landscape_next_button);
        mNextWeekButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar c = Calendar.getInstance();
                c.setTime(mFirstDateOfWeek);
                c.add(Calendar.WEEK_OF_YEAR, 1);
                mCurrentDate = c.getTime();
                mFirstDateOfWeek = c.getTime();
                updateUI();
            }
        });

        updateUI();

        return view;
    }

    private class AssignmentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mAssignmentName;
        private TextView mSundayTextView;
        private TextView mMondayTextView;
        private TextView mTuesdayTextView;
        private TextView mWednesdayTextView;
        private TextView mThursdayTextView;
        private TextView mFridayTextView;
        private TextView mSaturdayTextView;

        private Assignment mAssignment;


        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), mAssignment.getName() + " clicked!", Toast.LENGTH_SHORT).show();
        }

        public AssignmentHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_assignment_week, parent, false));
            itemView.setOnClickListener(this);

            mAssignmentName = itemView.findViewById(R.id.assignment_name);
            mSundayTextView = itemView.findViewById(R.id.sunday);
            mMondayTextView = itemView.findViewById(R.id.monday);
            mTuesdayTextView = itemView.findViewById(R.id.tuesday);
            mWednesdayTextView = itemView.findViewById(R.id.wednesday);
            mThursdayTextView = itemView.findViewById(R.id.thursday);
            mFridayTextView = itemView.findViewById(R.id.friday);
            mSaturdayTextView = itemView.findViewById(R.id.saturday);
        }

        public void bind(Assignment assignment){
            mAssignment = assignment;
            mAssignmentName.setText(mAssignment.getName());

            // Get color
            AssignmentColorManager manager = AssignmentColorManager.getInstance();
            List<Integer> colors = manager.getColors();
            int workingColor = colors.get(0).intValue();
            int dueDateColor = colors.get(1).intValue();

            // Set color for each day of the week
            // Iterate over each day and fill with necessary color
            List<TextView> days = new ArrayList<>();
            days.add(mSundayTextView);
            days.add(mMondayTextView);
            days.add(mTuesdayTextView);
            days.add(mWednesdayTextView);
            days.add(mThursdayTextView);
            days.add(mFridayTextView);
            days.add(mSaturdayTextView);
            boolean hasBeenFilled = false;
            for (TextView day : days){
                day.setBackgroundColor(getResources().getColor(R.color.white));
            }
            for (int i = 0; !hasBeenFilled && i < days.size(); i++){
                // Get current date of the week
                Date dateOfWeek = mFirstDateOfWeek;
                Calendar c = Calendar.getInstance();
                c.setTime(dateOfWeek);
                c.add(Calendar.DAY_OF_YEAR, i);
                dateOfWeek = c.getTime();

                // Check if that date corresponds with a work day or due date
                Date date = null;
                String dueDate = null;
                try {
                    date = mDateFormat.parse(mAssignment.getStartDate());
                    dueDate = mDateFormat.format(mDateFormat.parse(mAssignment.getDueDate()));
                } catch(java.text.ParseException e){
                    e.printStackTrace();
                }
                boolean isWorkDay = false;
                while (!isWorkDay && !mDateFormat.format(date).equals(dueDate)){
                    if (mDateFormat.format(dateOfWeek).equals(mDateFormat.format(date))) {
                        isWorkDay = true;
                        days.get(i).setBackgroundColor(getResources().getColor(workingColor));
                    }
                    // Next day
                    c.setTime(date);
                    c.add(Calendar.DAY_OF_YEAR, 1);
                    date = c.getTime();
                }
                if (!isWorkDay && mDateFormat.format(dateOfWeek).equals(dueDate)){
                    hasBeenFilled = true;
                    days.get(i).setBackgroundColor(getResources().getColor(dueDateColor));
                }
            }

        }
    }

    private void updateUI(){
        // Get last day of week
        Calendar c = Calendar.getInstance();
        c.setTime(mFirstDateOfWeek);
        c.add(Calendar.DAY_OF_YEAR, 6);
        Date lastDayOfWeek = c.getTime();

        // Set Current week
        mCurrentWeekText.setText("Week of " + mDateFormat.format(mFirstDateOfWeek) + " - " + mDateFormat.format(lastDayOfWeek));

        // Get all assignments due this day
        List<Assignment> assignments = AssignmentList.INSTANCE.getMAssignments();
        List<Assignment> weekAssignments = getWeekAssignments(assignments);

        mDueAssignmentAdapter = new AssignmentAdapter(weekAssignments);
        mAssignmentRecyclerView.setAdapter(mDueAssignmentAdapter);
    }

    // Get all assignments worked on in the current week
    private List<Assignment> getWeekAssignments(List<Assignment> assignments){
        List<Assignment> weekAssignments = new ArrayList<>();
        for(Assignment assignment : assignments){
            // Get the start date of the assignment
            Date date = null;
            String dueDate = null;
            try {
                date = mDateFormat.parse(assignment.getStartDate());
                dueDate = mDateFormat.format(mDateFormat.parse(assignment.getDueDate()));
            } catch(java.text.ParseException e){
                e.printStackTrace();
            }

            // Iterate from start date to current date
            // determine if it is in current week
            Boolean isInNextWeek = false;
            while (!isInNextWeek && !mDateFormat.format(date).equals(dueDate)){
                // Check each date of the current week to see if the date is in it
                int dateAfter = 0;
                while (!isInNextWeek && dateAfter < 7){
                    // Add the the number of day past the first day of the week to get day of week
                    Calendar c = Calendar.getInstance();
                    c.setTime(mFirstDateOfWeek);
                    c.add(Calendar.DAY_OF_YEAR, dateAfter);
                    String dayOfWeek = mDateFormat.format(c.getTime());

                    // Get day as string
                    String testDate = mDateFormat.format(date);

                    if (testDate.equals(dayOfWeek)){
                        isInNextWeek = true;
                    }
                    dateAfter += 1;
                }

                if (isInNextWeek){
                    weekAssignments.add(assignment);
                }

                // Next day
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DAY_OF_YEAR, 1);
                date = c.getTime();
            }
        }
        return weekAssignments;
    }


    private class AssignmentAdapter extends RecyclerView.Adapter<AssignmentHolder>{
        private List<Assignment> mAssignments;

        public AssignmentAdapter(List<Assignment> assignments) { mAssignments = assignments; }

        @NonNull
        @Override
        public AssignmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new AssignmentHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull AssignmentHolder holder, int position) {
            Assignment assignment = mAssignments.get(position);
            holder.bind(assignment);
        }

        @Override
        public int getItemCount() {
            return mAssignments.size();
        }
    }

}

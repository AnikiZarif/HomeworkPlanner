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

            // TODO: Change this to be systematic
            int workingColor = R.color.working1;
            int dueDateColor = R.color.due1;

            // Set color for each day of the week
            // TODO: set according to assignment duedate
            mSundayTextView.setBackgroundColor(getResources().getColor(workingColor));
            mMondayTextView.setBackgroundColor(getResources().getColor(workingColor));
            mTuesdayTextView.setBackgroundColor(getResources().getColor(workingColor));
            mWednesdayTextView.setBackgroundColor(getResources().getColor(workingColor));
            mThursdayTextView.setBackgroundColor(getResources().getColor(workingColor));
            mFridayTextView.setBackgroundColor(getResources().getColor(workingColor));
            mSaturdayTextView.setBackgroundColor(getResources().getColor(dueDateColor));
        }
    }

    private void updateUI(){
        // Get last day of week
        Calendar c = Calendar.getInstance();
        c.setTime(mFirstDateOfWeek);
        c.add(Calendar.DAY_OF_YEAR, 6);
        Date lastDayOfWeek = c.getTime();

        // Set Current week
        mCurrentWeekText.setText("Week of " + mDateFormat.format(mCurrentDate) + " - " + mDateFormat.format(lastDayOfWeek));

        // Get all assignments due this day
        List<Assignment> assignments = AssignmentList.INSTANCE.getMAssignments();
        List<Assignment> assignmentsDue = getAssignmentsDue(assignments);

        // Get all assignments due in the next week
        List<Assignment> upcomingAssignments = getUpcomingAssignments(assignments);

        mDueAssignmentAdapter = new AssignmentAdapter(assignmentsDue);
        mAssignmentRecyclerView.setAdapter(mDueAssignmentAdapter);
    }

    // Get all assignments due in the next week
    private List<Assignment> getUpcomingAssignments(List<Assignment> assignments){
        List<Assignment> upcomingAssignments = new ArrayList<>();
        for(Assignment assignment : assignments){
            Boolean isInNextWeek = false;
            int dateAfter = 1;
            while (!isInNextWeek && dateAfter <= 7){
                Calendar c = Calendar.getInstance();
                c.setTime(mCurrentDate);
                c.add(Calendar.DAY_OF_YEAR, dateAfter);
                String dateAfterCurrentDate = mDateFormat.format(c.getTime());
                Date dueDate = null;
                try{
                    dueDate = mDateFormat.parse(assignment.getDueDate());
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                String assignmentDueDate = mDateFormat.format(dueDate);
                if (dateAfterCurrentDate.equals(assignmentDueDate)){
                    isInNextWeek = true;
                }
                dateAfter += 1;
            }
            if (isInNextWeek){
                upcomingAssignments.add(assignment);
            }
        }
        return upcomingAssignments;
    }


    // Get all assignments due on the current day
    private List<Assignment> getAssignmentsDue(List<Assignment> assignments){
        List<Assignment> assignmentsDue = new ArrayList<>();
        for (Assignment assignment : assignments) {
            Date dueDate = null;
            try{
                dueDate = mDateFormat.parse(assignment.getDueDate());
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            String currentDate = mDateFormat.format(mCurrentDate);
            String assignmentDueDate = mDateFormat.format(dueDate);
            if (currentDate.equals(assignmentDueDate)){
                assignmentsDue.add(assignment);
            }
        }
        return assignmentsDue;
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

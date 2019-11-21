package com.mobileappdev.homeworkplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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


public class PortraitCalendarFragment extends Fragment {

    private RecyclerView mAssignmentRecyclerView;
    private RecyclerView mUpcomingAssignmentRecyclerView;
    private AssignmentAdapter mDueAssignmentAdapter;
    private AssignmentAdapter mUpcomingAssignmentAdapter;
    private TextView mCurrentDateText;
    private Button mNextDateButton;
    private Button mPrevDateButton;

    private Date mCurrentDate = null;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM/dd/yyyy");



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_portait_calendar, container, false);

        // Get today's date
        if (mCurrentDate == null){
            Date today = new Date();
            mCurrentDate = today;
        }

        mAssignmentRecyclerView = (RecyclerView) view.findViewById(R.id.assignment_recycler_view);
        mAssignmentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mUpcomingAssignmentRecyclerView = view.findViewById(R.id.upcoming_assignment_recycler_view);
        mUpcomingAssignmentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mCurrentDateText = view.findViewById(R.id.portrait_view_date);

        // Set prev date
        mPrevDateButton = view.findViewById(R.id.portrait_prev_button);
        mPrevDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                c.setTime(mCurrentDate);
                c.add(Calendar.DAY_OF_YEAR, -1);
                mCurrentDate = c.getTime();
                updateUI();
            }
        });

        // Set next date
        mNextDateButton = view.findViewById(R.id.portrait_next_button);
        mNextDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar c = Calendar.getInstance();
                c.setTime(mCurrentDate);
                c.add(Calendar.DAY_OF_YEAR, 1);
                mCurrentDate = c.getTime();
                updateUI();
            }
        });

        updateUI();

        return view;
    }

    private class AssignmentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mAssignmentTitleTextView;
        private Assignment mAssignment;
        private TextView mClassTitleTextView;
        private LinearLayout mAssignmentLinearLayout;


        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), mAssignment.getName() + " clicked!", Toast.LENGTH_SHORT).show();
        }

        public AssignmentHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_assignment, parent, false));
            itemView.setOnClickListener(this);

            mAssignmentTitleTextView = (TextView)itemView.findViewById(R.id.assignment_title);
            mClassTitleTextView = itemView.findViewById(R.id.class_title);
            mAssignmentLinearLayout = itemView.findViewById(R.id.assignment_view);
        }

        public void bind(Assignment assignment){
            mAssignment = assignment;
            mAssignmentTitleTextView.setText(mAssignment.getName());
            mClassTitleTextView.setText(mAssignment.getParentClass() + ":");
            String importance = mAssignment.getImportance();
            if (importance.equals("Very Important")){
                mAssignmentLinearLayout.setBackgroundColor(getResources().getColor(R.color.veryImportantAssignment));
            } else if (importance.equals("Important")){
                mAssignmentLinearLayout.setBackgroundColor(getResources().getColor(R.color.importantAssignment));
            } else {
                mAssignmentLinearLayout.setBackgroundColor(getResources().getColor(R.color.normalAssignment));
            }
        }
    }

    private void updateUI(){
        // Set prev date
        Calendar c = Calendar.getInstance();
        c.setTime(mCurrentDate);
        c.add(Calendar.DAY_OF_YEAR, -1);
        mPrevDateButton.setText(mDateFormat.format(c.getTime()));

        // Set next date
        c.setTime(mCurrentDate);
        c.add(Calendar.DAY_OF_YEAR, 1);
        mNextDateButton.setText(mDateFormat.format(c.getTime()));

        // Set Current date
        mCurrentDateText.setText(mDateFormat.format(mCurrentDate));

        // Get all assignments due this day
        List<Assignment> assignments = AssignmentList.INSTANCE.getMAssignments();
        List<Assignment> assignmentsDue = getAssignmentsDue(assignments);

        // Get all assignments due in the next week
        List<Assignment> upcomingAssignments = getUpcomingAssignments(assignments);

        mUpcomingAssignmentAdapter = new AssignmentAdapter(upcomingAssignments);
        mDueAssignmentAdapter = new AssignmentAdapter(assignmentsDue);
        mAssignmentRecyclerView.setAdapter(mDueAssignmentAdapter);
        mUpcomingAssignmentRecyclerView.setAdapter(mUpcomingAssignmentAdapter);
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

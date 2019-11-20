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


public class PortraitCalendarFragment extends Fragment {

    private RecyclerView mAssignmentRecyclerView;
    private AssignmentAdapter mAdapter;
    private Button mAddAssignmentButton;
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
        mCurrentDateText = view.findViewById(R.id.portrait_view_date);
        mCurrentDateText.setText(mDateFormat.format(mCurrentDate));

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
        private TextView mTitleTextView;
        private Assignment mAssignment;


        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), mAssignment.getName() + " clicked!", Toast.LENGTH_SHORT).show();
        }

        public AssignmentHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_assignment, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView)itemView.findViewById(R.id.assignment_title);
        }

        public void bind(Assignment assignment){
            mAssignment = assignment;
            mTitleTextView.setText(mAssignment.getName());
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

        List<Assignment> assignments = AssignmentList.INSTANCE.getMAssignments();

        List<Assignment> assignmentsDue = getAssignmentsDue(assignments);
//        List<Assignment> assignmentsDue = assignments;

        mAdapter = new AssignmentAdapter(assignmentsDue);
        mAssignmentRecyclerView.setAdapter(mAdapter);
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
            System.out.println(currentDate);
            System.out.println(assignmentDueDate);
            System.out.println(currentDate.equals(assignmentDueDate));
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

package com.mobileappdev.homeworkplanner;

import android.content.Intent;
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

import java.util.List;

public class PortraitCalendarFragment extends Fragment {

    private RecyclerView mAssignmentRecyclerView;
    private AssignmentAdapter mAdapter;
    private Button mAddAssignmentButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_portait_calendar, container, false);

        mAssignmentRecyclerView = (RecyclerView) view.findViewById(R.id.assignment_recycler_view);
        mAssignmentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
        AssignmentList assignmentList = AssignmentList.get(getActivity());
        List<Assignment> assignments = assignmentList.getAssignments();

        mAdapter = new AssignmentAdapter(assignments);
        mAssignmentRecyclerView.setAdapter(mAdapter);
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

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
    private Button mAddClassButton;

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
        private Class mClass;


        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), mClass.getTitile() + " clicked!", Toast.LENGTH_SHORT).show();
        }

        public ClassHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_class, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView)itemView.findViewById(R.id.class_title);
        }

        public void bind(Class aClass){
            mClass = aClass;
            mTitleTextView.setText(mClass.getTitile());
        }
    }

    private void updateUI(){
        ClassSchedule classSchedule = ClassSchedule.get(getActivity());
        List<Class> classes = classSchedule.getClasses();

        mAdapter = new ClassListFragment.ClassAdapter(classes);
        mAssignmentRecyclerView.setAdapter(mAdapter);
    }

    private class AssignmentAdapter extends RecyclerView.Adapter<AssignmentHolder>{
        private List<Class> mClasses;

        public ClassAdapter(List<Class> aClasses) { mClasses = aClasses; }

        @NonNull
        @Override
        public ClassListFragment.ClassHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new ClassListFragment.ClassHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ClassListFragment.ClassHolder holder, int position) {
            Class aClass = mClasses.get(position);
            holder.bind(aClass);
        }

        @Override
        public int getItemCount() {
            return mClasses.size();
        }
    }
}

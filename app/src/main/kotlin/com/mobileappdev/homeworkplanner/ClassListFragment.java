package com.mobileappdev.homeworkplanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class ClassListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private ClassAdapter mAdapter;
    private Button mAddClassButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_list, container, false);

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.class_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAddClassButton = (Button) view.findViewById(R.id.add_class_button);
        mAddClassButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), AddClassActivity.class);
                startActivity(intent);
            }}
        );

        updateUI();

        return view;
    }

    private class ClassHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

        mAdapter = new ClassAdapter(classes);
        mCrimeRecyclerView.setAdapter(mAdapter);
    }

    private class ClassAdapter extends RecyclerView.Adapter<ClassHolder>{
        private List<Class> mClasses;

        public ClassAdapter(List<Class> aClasses) { mClasses = aClasses; }

        @NonNull
        @Override
        public ClassHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new ClassHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ClassHolder holder, int position) {
            Class aClass = mClasses.get(position);
            holder.bind(aClass);
        }

        @Override
        public int getItemCount() {
            return mClasses.size();
        }
    }
}

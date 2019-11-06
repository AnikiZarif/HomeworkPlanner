package com.mobileappdev.homeworkplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.myapplication.R;

public class ClassListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.class_list_fragment_container);

        if (fragment == null){
            fragment = new ClassListFragment();
            fm.beginTransaction().add(R.id.class_list_fragment_container, fragment).commit();
        }
    }
}
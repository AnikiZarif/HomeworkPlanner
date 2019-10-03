package com.example.myapplication;

import androidx.fragment.app.Fragment;

public class AddClassActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new AddClassFragment();
    }
}
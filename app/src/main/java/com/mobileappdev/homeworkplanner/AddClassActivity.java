package com.mobileappdev.homeworkplanner;

import androidx.fragment.app.Fragment;

public class AddClassActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new AddClassFragment();
    }
}
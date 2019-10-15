package com.mobileappdev.homeworkplanner;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

public class AddClassFragment extends Fragment implements View.OnClickListener {
    private EditText className;
    private Spinner creditHours;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        Activity activity = getActivity();

        if (activity != null) {
            int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
            if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
                // TODO: fragment for landscape mode
                v = inflater.inflate(R.layout.fragment_add_class, container, false);
            } else {
                v = inflater.inflate(R.layout.fragment_add_class, container, false);
            }
        }
        else {
            v = inflater.inflate(R.layout.fragment_add_class, container, false);
        }

        className = v.findViewById(R.id.class_name_text);
        creditHours = v.findViewById(R.id.credit_hours_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.credit_hours_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        creditHours.setAdapter(adapter);

        Button timeButton = v.findViewById(R.id.time_button);
        if (timeButton != null) {
            timeButton.setOnClickListener(this);
        }
        Button dateButton = v.findViewById(R.id.date_button);
        if (dateButton != null) {
            dateButton.setOnClickListener(this);
        }

        return v;
    }

    @Override
    public void onClick(View view) {

    }
}

package com.mobileappdev.homeworkplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.myapplication.R

class AddClassFragment: Fragment(), View.OnClickListener {
    private lateinit var mClassNameEditText: EditText
    private lateinit var mCreditHoursSpinner: Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View
        val rotation = activity?.windowManager?.defaultDisplay?.rotation
        v = if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            // TODO: Create add class landscape fragment
            inflater.inflate(R.layout.fragment_add_class, container, false)
        } else {
            inflater.inflate(R.layout.fragment_add_class, container, false)
        }

        mClassNameEditText = v.findViewById(R.id.class_name_text)
        mCreditHoursSpinner = v.findViewById(R.id.credit_hours_spinner)

        val adapter = ArrayAdapter.createFromResource(context!!,
                R.array.credit_hours_options, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mCreditHoursSpinner.adapter = adapter

        val classTimeButton : Button = v.findViewById(R.id.time_button)
        classTimeButton.setOnClickListener(this)
        val classDateButton : Button = v.findViewById(R.id.date_button)
        classDateButton.setOnClickListener(this)

        return v
    }

    fun showTimePickerDialog(view: View) {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(activity!!.supportFragmentManager,"timePicker")
    }

    fun showDatePickerDialog(view: View) {
        val datePickerFragment = DatePickerFragment()
        datePickerFragment.show(activity!!.supportFragmentManager,"datePicker")
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.time_button -> showTimePickerDialog(view)
            R.id.date_button -> showDatePickerDialog(view)
        }
    }

}
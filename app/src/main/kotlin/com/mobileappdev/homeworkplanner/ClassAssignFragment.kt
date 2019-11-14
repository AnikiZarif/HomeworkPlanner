package com.mobileappdev.homeworkplanner

import android.content.Intent
import androidx.fragment.app.Fragment

abstract class ClassAssignFragment: Fragment() {
    fun showTimePickerDialog(tag: String) {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.setTargetFragment(this, 0)
        timePickerFragment.show(activity!!.supportFragmentManager, tag)
        //Log.d("lifecycle", "Timer invoked")
    }

    abstract fun onTimePickerReturn(data: Intent, name: String)

    abstract fun addToFirestore()
}
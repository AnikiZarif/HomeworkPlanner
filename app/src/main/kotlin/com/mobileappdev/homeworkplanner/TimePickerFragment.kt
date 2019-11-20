package com.mobileappdev.homeworkplanner

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity!!, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val intent = Intent()
        var minuteStr = minute.toString()
        if (minute < 10) {
            minuteStr = "0$minute"
        }
        val time = "$hourOfDay:$minuteStr"
        var name = "com.mobileappdev.homeworkplanner.starttime"
        if (this.tag == "endTime") {
            name = "com.mobileappdev.homeworkplanner.endtime"
        } else if (this.tag == "dueTime") {
            name = "com.mobileappdev.homeworkplanner.duetime"
        }
        intent.putExtra(name, time)
        val tf = targetFragment as ClassAssignFragment
        tf.onTimePickerReturn(intent, name)
    }
}
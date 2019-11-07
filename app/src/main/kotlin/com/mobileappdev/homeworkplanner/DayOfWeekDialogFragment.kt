package com.mobileappdev.homeworkplanner

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R

class DayOfWeekDialogFragment: DialogFragment() {
    private lateinit var selectedItems: ArrayList<Int>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            selectedItems = ArrayList() // Where we track the selected items
            builder.setTitle(R.string.pick_day)
                    .setMultiChoiceItems(R.array.days_of_week, null
                    ) { _, which, isChecked ->
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            selectedItems.add(which)
                        } else if (selectedItems.contains(which)) {
                            // Else, if the item is already in the array, remove it
                            selectedItems.remove(Integer.valueOf(which))
                        }
                    }
                    .setPositiveButton(R.string.ok
                    ) { dialog, _ ->
                        sendData()
                        dialog.dismiss()
                    }
                    .setNegativeButton(R.string.cancel
                    ) { dialog, _ ->
                        selectedItems.clear()
                        dialog.dismiss()
                    }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun sendData() {
        val intent = Intent()
        intent.putExtra("com.mobileappdev.homeworkplanner.days", selectedItems)
        val tf = targetFragment as AddClassFragment
        tf.onDayPickerReturn(intent)
    }
}
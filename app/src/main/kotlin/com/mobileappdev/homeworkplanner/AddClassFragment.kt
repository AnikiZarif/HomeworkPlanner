package com.mobileappdev.homeworkplanner

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddClassFragment: ClassAssignFragment(), View.OnClickListener {
    private lateinit var mClassNameEditText: EditText
    private lateinit var mDaysTextView: TextView
    private lateinit var mStartTimeTextView: TextView
    private lateinit var mEndTimeTextView: TextView
    private lateinit var mCreditHoursSpinner: Spinner
    private lateinit var mAddClassButton: Button
    private lateinit var mClassDateButton : Button
    private lateinit var mClassStartTimeButton : Button
    private lateinit var mClassEndTimeButton : Button
    private lateinit var classArray: ArrayList<String>

    private val db = FirebaseFirestore.getInstance()
    private val TAG = AddClassFragment::class.java.simpleName
    private var uid : String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):View? {
        val v: View
        val rotation = activity?.windowManager?.defaultDisplay?.rotation
        v = if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            // TODO: Create add class landscape fragment
            inflater.inflate(R.layout.fragment_add_class, container, false)
        } else {
            inflater.inflate(R.layout.fragment_add_class, container, false)
        }

        Log.d(TAG,"AddClassFragment invoked")
        mClassNameEditText = v.findViewById(R.id.class_name_text)
        mCreditHoursSpinner = v.findViewById(R.id.credit_hours_spinner)
        mDaysTextView = v.findViewById(R.id.days_text)
        mStartTimeTextView = v.findViewById(R.id.start_time_text)
        mEndTimeTextView = v.findViewById(R.id.end_time_text)

        val adapter = ArrayAdapter.createFromResource(context!!,
                R.array.credit_hours_options, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mCreditHoursSpinner.adapter = adapter

        mClassStartTimeButton = v.findViewById(R.id.start_time_button)
        mClassStartTimeButton.setOnClickListener(this)

        mClassEndTimeButton = v.findViewById(R.id.end_time_button)
        mClassEndTimeButton.setOnClickListener(this)

        mClassDateButton = v.findViewById(R.id.date_button)
        mClassDateButton.setOnClickListener(this)

        mAddClassButton = v.findViewById(R.id.add_button)
        mAddClassButton.setOnClickListener(this)

        return v
    }

    private fun showDayPickerDialog() {
        val dayOfWeekDialogFragment = DayOfWeekDialogFragment()
        dayOfWeekDialogFragment.setTargetFragment(this, 0)
        dayOfWeekDialogFragment.show(activity!!.supportFragmentManager, "dayPicker")
    }

    override fun onTimePickerReturn(data: Intent, name: String) {
        if (name == "com.mobileappdev.homeworkplanner.starttime") {
            mStartTimeTextView.text = data.getStringExtra(name)
        } else {
            mEndTimeTextView.text = data.getStringExtra(name)
        }
    }

    fun onDayPickerReturn(data: Intent) {
        val daysPicked = data.getIntegerArrayListExtra("com.mobileappdev.homeworkplanner.days")
        classArray = ArrayList()
        var count = 0
        val dayArray: Array<String> = resources.getStringArray(R.array.days_of_week)
        daysPicked!!.forEach {
            classArray.add(dayArray[it])
            if (count == 0) {
                mDaysTextView.text = dayArray[it]
            } else {
                mDaysTextView.text = getString(R.string.day_list, mDaysTextView.text, dayArray[it])
            }
            count++
        }
    }

    override fun addToFirestore(){
        val className = mClassNameEditText.text.toString()
        val creds = mCreditHoursSpinner.selectedItem.toString().toLong()
        val classStartTime = mStartTimeTextView.text.toString()
        val classEndTime = mEndTimeTextView.text.toString()
        val item = hashMapOf(
                "className" to className,
                "creditHours" to creds,
                "dayOfWeek" to classArray,
                "classStartTime" to classStartTime,
                "classEndTime" to classEndTime
        )

        if(FirebaseAuth.getInstance().currentUser != null){
            uid = FirebaseAuth.getInstance().currentUser!!.uid
            Log.d(TAG,"UID = $uid")
        }
        db.collection("user").document(uid).collection("classes")
                .add(item)
                .addOnSuccessListener{DocumentReference->
                    ClassSchedule.createClass(item)
                    Log.d(TAG, "Added class with ${DocumentReference.id}")
                    Toast.makeText(activity!!,"Added Class",Toast.LENGTH_LONG).show()
                    activity!!.finish()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

    }

    
    override fun onClick(v: View) {
        when (v.id) {
            R.id.start_time_button -> showTimePickerDialog("startTime")
            R.id.end_time_button -> showTimePickerDialog("endTime")
            R.id.date_button -> showDayPickerDialog()
            R.id.add_button -> addToFirestore()
        }
    }

}
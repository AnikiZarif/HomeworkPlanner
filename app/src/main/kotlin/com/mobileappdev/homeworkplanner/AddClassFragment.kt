package com.mobileappdev.homeworkplanner

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class AddClassFragment: Fragment(), View.OnClickListener {
    private lateinit var mClassNameEditText: EditText
    private lateinit var mDaysTextView: TextView
    private lateinit var mCreditHoursSpinner: Spinner
    private lateinit var mAddClassButton: Button
    private lateinit var mClassDateButton : Button
    private lateinit var mClassTimeButton : Button
    private lateinit var mDeleteClassButton : Button
    private lateinit var classArray: ArrayList<String>

    private val db = FirebaseFirestore.getInstance()
    private val TAG = AddClassFragment::class.java!!.getSimpleName()
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

        val adapter = ArrayAdapter.createFromResource(context!!,
                R.array.credit_hours_options, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mCreditHoursSpinner.adapter = adapter

        mClassTimeButton = v.findViewById(R.id.time_button)
        mClassTimeButton.setOnClickListener(this)

        mClassDateButton = v.findViewById(R.id.date_button)
        mClassDateButton.setOnClickListener(this)

        mAddClassButton = v.findViewById(R.id.add_button)
        mAddClassButton.setOnClickListener(this)

        mDeleteClassButton = v.findViewById(R.id.delete_button)
        mDeleteClassButton.setOnClickListener(this)

        return v
    }

    fun showTimePickerDialog() {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(activity!!.supportFragmentManager,"timePicker")
        //Log.d("lifecycle", "Timer invoked")
    }

    fun showDayPickerDialog() {
        val dayOfWeekDialogFragment = DayOfWeekDialogFragment()
        dayOfWeekDialogFragment.setTargetFragment(this, 0)
        dayOfWeekDialogFragment.show(activity!!.supportFragmentManager, "dayPicker")
    }

    fun onDayPickerReturn(data: Intent) {
        val daysPicked = data.getIntegerArrayListExtra("com.mobileappdev.homeworkplanner.days")
        classArray = ArrayList()
        var count = 0
        var day = "MONDAY"
        daysPicked.forEach {
            when(it) {
                1 -> day = "MONDAY"
                2 -> day = "TUESDAY"
                3 -> day = "WEDNESDAY"
                4 -> day = "THURSDAY"
                5 -> day = "FRIDAY"
                6 -> day = "SATURDAY"
                7 -> day = "SUNDAY"
            }
            classArray.add(day)
            if (count == 0) {
                mDaysTextView.text = day
            } else {
                mDaysTextView.text = mDaysTextView.text.toString() + "," + day
            }
            count++
        }
    }

    fun addToFirestore(){
        val text = mClassNameEditText.text.toString()
        val creds = mCreditHoursSpinner.getSelectedItem().toString().toInt()
        val item = hashMapOf(
            "className" to text,
            "creditHours" to creds
        )

        if(FirebaseAuth.getInstance().currentUser != null){
            uid = FirebaseAuth.getInstance().currentUser!!.uid
            Log.d(TAG,"UID = $uid")
        }
        db.collection("user").document(uid).collection("classes")
                .add(item)
                .addOnSuccessListener{DocumentReference->
                    Log.d(TAG, "Added class with ${DocumentReference.id}");
                    Toast.makeText(activity!!,"Added Class",Toast.LENGTH_LONG).show();
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

    }

    fun deleteClass(){
        val text = mClassNameEditText.text.toString()
        db.collection("classes")
                .whereEqualTo("className", text)
                .get()
                .addOnSuccessListener {documents ->
                    db.collection("classes")
                            .document(documents.documents[0].reference.id)
                            .delete()
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
    }

    
    override fun onClick(v: View) {
        when (v.id) {
            R.id.time_button -> showTimePickerDialog()
            R.id.date_button -> showDayPickerDialog()
            R.id.add_button -> addToFirestore()
            R.id.delete_button -> deleteClass()
        }
    }

}
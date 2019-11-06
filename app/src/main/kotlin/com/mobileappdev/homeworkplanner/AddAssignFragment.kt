package com.mobileappdev.homeworkplanner

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
import com.google.firebase.firestore.FirebaseFirestore

class AddAssignFragment: Fragment(), View.OnClickListener {
    private lateinit var mAssignNameEditText: EditText
    private lateinit var mImportanceSpinner: Spinner
    private lateinit var mAddClassButton: Button
    private lateinit var mDueDateButton : Button
    private lateinit var mDueTimeButton : Button

    private val db = FirebaseFirestore.getInstance()
    private val TAG = AddAssignFragment::class.java!!.getSimpleName()
    private var uid : String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):View? {
        val v: View
        val rotation = activity?.windowManager?.defaultDisplay?.rotation
        v = if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            // TODO: Create add class landscape fragment
            inflater.inflate(R.layout.fragment_add_assign, container, false)
        } else {
            inflater.inflate(R.layout.fragment_add_assign, container, false)
        }

        Log.d(TAG,"AddClassFragment invoked")
        mAssignNameEditText = v.findViewById(R.id.assign_name_text)
        mImportanceSpinner = v.findViewById(R.id.credit_hours_spinner)

        val years = arrayOf("Very Important", "Important", "Normal")
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, years)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        mImportanceSpinner.adapter = adapter

        mDueTimeButton = v.findViewById(R.id.time_button)
        mDueTimeButton.setOnClickListener(this)

        mDueDateButton = v.findViewById(R.id.date_button)
        mDueDateButton.setOnClickListener(this)

        mAddClassButton = v.findViewById(R.id.add_assign)
        mAddClassButton.setOnClickListener(this)


        return v
        }


        fun showTimePickerDialog() {
            val timePickerFragment = TimePickerFragment()
            timePickerFragment.show(activity!!.supportFragmentManager,"timePicker")
            //Log.d("lifecycle", "Timer invoked")
        }

        fun showDayPickerDialog() {
            val dayOfWeekDialogFragment = DayOfWeekDialogFragment()
            dayOfWeekDialogFragment.show(activity!!.supportFragmentManager, "dayPicker")
        }

        fun addToFirestore(){
            val text = mAssignNameEditText.text.toString()
            val imp = mImportanceSpinner.getSelectedItem().toString()
            //val dueTime = mDueTimeButton.
            val item = hashMapOf(
                    "assign_name" to text,
                    "imp" to imp
            )

            //NEED TO ADJUST THIS SO WE CAN ADD ASSIGNMENT UNDER A CLASS
            if(FirebaseAuth.getInstance().currentUser != null){
                uid = FirebaseAuth.getInstance().currentUser!!.uid
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
        override fun onClick(v: View) {
            when (v.id) {
                R.id.time_button -> showTimePickerDialog()
                R.id.date_button -> showDayPickerDialog()
                R.id.add_button -> addToFirestore()
            }
        }
    }
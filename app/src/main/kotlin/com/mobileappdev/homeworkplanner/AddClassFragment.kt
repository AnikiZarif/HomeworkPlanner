package com.mobileappdev.homeworkplanner

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

class
AddClassFragment: Fragment(), View.OnClickListener {
    private lateinit var mClassNameEditText: EditText
    private lateinit var mCreditHoursSpinner: Spinner
    private val db = FirebaseFirestore.getInstance()
    //private lateinit var mAddClassButton: Button

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
        //mAddClassButton = v.findViewById(R.id.add_button)

        val adapter = ArrayAdapter.createFromResource(context!!,
                R.array.credit_hours_options, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mCreditHoursSpinner.adapter = adapter

        val classTimeButton : Button = v.findViewById(R.id.time_button)
        classTimeButton.setOnClickListener(this)
        val classDateButton : Button = v.findViewById(R.id.date_button)
        classDateButton.setOnClickListener(this)
        val addClassButton = v.findViewById(R.id.add_button) as Button
        addClassButton.setOnClickListener{
            addToFirestore()
        }

        return v
    }

    fun showTimePickerDialog(view: View) {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(activity!!.supportFragmentManager,"timePicker")
        Log.d("lifecycle", "Timer invoked")
    }

    fun showDatePickerDialog(view: View) {
        val datePickerFragment = DatePickerFragment()
        datePickerFragment.show(activity!!.supportFragmentManager,"datePicker")
    }

    fun addToFirestore(){
        var text = mClassNameEditText.text.toString()

        var item = hashMapOf(
            "name" to text
        )
        db.collection("classes")
            .add(item)
                .addOnSuccessListener{
                    documentReference -> Log.d("lifecycle", "Document added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("lifecycle", "Error adding document", e)
                }
        //Toast.makeText(this@AddClassFragment,text.toString(),Toast.LENGTH_LONG).show()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.time_button -> showTimePickerDialog(view)
            R.id.date_button -> showDatePickerDialog(view)
        }
    }

}
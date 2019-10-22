package com.mobileappdev.homeworkplanner
import kotlinx.android.synthetic.main.activity_main.view.*
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
AddClassFragment: Fragment() {
    private lateinit var mClassNameEditText: EditText
    private lateinit var mCreditHoursSpinner: Spinner
    private lateinit var mAddClassButton: Button
    private lateinit var mClassDateButton : Button
    private lateinit var mClassTimeButton : Button
    private lateinit var mDeleteClassButton : Button
    private val db = FirebaseFirestore.getInstance()

    private val TAG = AddClassFragment::class.java!!.getSimpleName()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):View? {
        val v: View
        val rotation = activity?.windowManager?.defaultDisplay?.rotation
        v = if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            // TODO: Create add class landscape fragment
            inflater.inflate(R.layout.fragment_add_class, container, false)
        } else {
            inflater.inflate(R.layout.fragment_add_class, container, false)
        }

        Log.d(TAG,"CLass ${v}")
        mClassNameEditText = v.findViewById(R.id.class_name_text)
        mCreditHoursSpinner = v.findViewById(R.id.credit_hours_spinner)

        val adapter = ArrayAdapter.createFromResource(context!!,
                R.array.credit_hours_options, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mCreditHoursSpinner.adapter = adapter

        val clickListener = View.OnClickListener {view ->
            when (view.getId()) {
                R.id.time_button -> showTimePickerDialog(v)
                R.id.date_button -> showDatePickerDialog(v)
                R.id.add_button -> addToFirestore()
            }
        }
        mClassTimeButton = v.findViewById(R.id.time_button)
        mClassTimeButton.setOnClickListener(clickListener)

        mClassDateButton = v.findViewById(R.id.date_button)
        mClassDateButton.setOnClickListener(clickListener)

        mAddClassButton = v.findViewById(R.id.add_button)
        mAddClassButton.setOnClickListener(clickListener)

        mDeleteClassButton = v.findViewById(R.id.delete_button)
        mDeleteClassButton.setOnClickListener(clickListener)

        return v
    }

    fun showTimePickerDialog(view: View) {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(activity!!.supportFragmentManager,"timePicker")
        //Log.d("lifecycle", "Timer invoked")
    }

    fun showDatePickerDialog(view: View) {
        val datePickerFragment = DatePickerFragment()
        datePickerFragment.show(activity!!.supportFragmentManager,"datePicker")
    }

    fun addToFirestore(){
        var text = mClassNameEditText.text.toString()
        var creds = mCreditHoursSpinner.getSelectedItem().toString().toInt()
        var item = hashMapOf(
            "className" to text,
            "creditHours" to creds
        )
        db.collection("classes")
            .add(item)
                .addOnSuccessListener{
                    documentReference -> Log.d(TAG, "Document added with ID: ${documentReference.id}");
                    Toast.makeText(activity!!,"Added Class",Toast.LENGTH_LONG).show();
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

    }
 /*
    fun deleteClass(view: View){
        
    }
    
  */
/*
    override fun onClick() {
        when (view.id) {
            R.id.time_button -> showTimePickerDialog(view)
            R.id.date_button -> showDatePickerDialog(view)
            R.id.add_button -> addToFirestore()
        }
    }
    */

}
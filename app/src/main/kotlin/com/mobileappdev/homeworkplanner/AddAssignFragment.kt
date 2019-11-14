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

class AddAssignFragment: ClassAssignFragment(), View.OnClickListener {
    private lateinit var mAssignNameEditText: EditText
    private lateinit var mImportanceSpinner: Spinner
    private lateinit var mAddClassButton: Button
    private lateinit var mDueDateButton : Button
    private lateinit var mDueTimeButton : Button
    private lateinit var mDueDateTextView: TextView
    private lateinit var mDueTimeTextView: TextView

    private val db = FirebaseFirestore.getInstance()
    private val TAG = AddAssignFragment::class.java.simpleName
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

        Log.d(TAG,"AddAssignFragment invoked")
        mAssignNameEditText = v.findViewById(R.id.assign_name_text)
        mImportanceSpinner = v.findViewById(R.id.importance_spinner)

        val years = arrayOf("Very Important", "Important", "Normal")
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, years)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        mImportanceSpinner.adapter = adapter

        mDueTimeButton = v.findViewById(R.id.due_time_button)
        mDueTimeButton.setOnClickListener(this)

        mDueTimeTextView = v.findViewById(R.id.due_time_text)

        mDueDateButton = v.findViewById(R.id.due_date_button)
        mDueDateButton.setOnClickListener(this)

        mDueDateTextView = v.findViewById(R.id.due_date_text)

        mAddClassButton = v.findViewById(R.id.add_assign)
        mAddClassButton.setOnClickListener(this)


        return v
    }

    override fun onTimePickerReturn(data: Intent, name: String) {
        mDueTimeTextView.text = data.getStringExtra(name)
    }

    fun showDatePickerDialog() {
        val datePickerFragment = DatePickerFragment()
        datePickerFragment.setTargetFragment(this, 0)
        datePickerFragment.show(activity!!.supportFragmentManager, "datePicker")
    }

    fun onDatePickerReturn(data: Intent, name: String) {
        mDueDateTextView.text = data.getStringExtra(name)
    }

    override fun addToFirestore(){
        val assignName = mAssignNameEditText.text.toString()
        val imp = mImportanceSpinner.selectedItem.toString()
        val dueTime = mDueTimeTextView.text.toString()
        val dueDate = mDueDateTextView.text.toString()
        val item = hashMapOf(
                "assignName" to assignName,
                "importance" to imp,
                "dueTime" to dueTime,
                "dueDate" to dueDate
        )
        //NEED TO ADJUST THIS SO WE CAN ADD ASSIGNMENT UNDER A CLASS
        if(FirebaseAuth.getInstance().currentUser != null){
            uid = FirebaseAuth.getInstance().currentUser!!.uid
        }
        db.collection("user").document(uid).collection("classes")
                .add(item)
                .addOnSuccessListener{DocumentReference->
                    Log.d(TAG, "Added assignment with ${DocumentReference.id}")
                    Toast.makeText(activity!!,"Added Assignment",Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.due_time_button -> showTimePickerDialog("dueTime")
            R.id.due_date_button -> showDatePickerDialog()
            R.id.add_button -> addToFirestore()
        }
    }
}
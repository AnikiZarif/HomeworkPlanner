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
    private lateinit var mTimeEstimateEditText: EditText
    private lateinit var mImportanceSpinner: Spinner
    private lateinit var mClassNameSpinner: Spinner
    private lateinit var mAddAssignmentButton: Button
    private lateinit var mDueDateButton : Button
    private lateinit var mStartDateButton : Button
    private lateinit var mDueTimeButton : Button
    private lateinit var mDueDateTextView: TextView
    private lateinit var mDueTimeTextView: TextView
    private lateinit var mStartDateTextView: TextView

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
        mClassNameSpinner = v.findViewById(R.id.class_spinner)
        mTimeEstimateEditText = v.findViewById(R.id.assign_time_estimate_text)

        val years = arrayOf("Very Important", "Important", "Normal")
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, years)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val classNames = ArrayList<String>()
        for (c in ClassSchedule.mClasses) {
            classNames.add(c.className!!)
        }
        val classNamesAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, classNames)
        classNamesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        mImportanceSpinner.adapter = adapter
        mClassNameSpinner.adapter = classNamesAdapter

        mDueTimeButton = v.findViewById(R.id.due_time_button)
        mDueTimeButton.setOnClickListener(this)

        mDueTimeTextView = v.findViewById(R.id.due_time_text)

        mDueDateButton = v.findViewById(R.id.due_date_button)
        mDueDateButton.setOnClickListener(this)

        mStartDateButton = v.findViewById(R.id.start_date_button)
        mStartDateButton.setOnClickListener(this)

        mDueDateTextView = v.findViewById(R.id.due_date_text)
        mStartDateTextView = v.findViewById(R.id.start_date_text)

        mAddAssignmentButton = v.findViewById(R.id.add_assign_button)
        mAddAssignmentButton.setOnClickListener(this)


        return v
    }

    override fun onTimePickerReturn(data: Intent, name: String) {
        mDueTimeTextView.text = data.getStringExtra(name)
    }

    fun showDatePickerDialog(tag: String) {
        val datePickerFragment = DatePickerFragment()
        datePickerFragment.setTargetFragment(this, 0)
        datePickerFragment.show(activity!!.supportFragmentManager, tag)
    }

    fun onDatePickerReturn(data: Intent, name: String) {
        if (name == "dueDate") {
            mDueDateTextView.text = data.getStringExtra(name)
        } else {
            mStartDateTextView.text = data.getStringExtra(name)
        }
    }

    override fun addToFirestore(){
        val className = mClassNameSpinner.selectedItem.toString()
        val assignName = mAssignNameEditText.text.toString()
        val imp = mImportanceSpinner.selectedItem.toString()
        val dueTime = mDueTimeTextView.text.toString()
        val dueDate = mDueDateTextView.text.toString()
        val startDate = mStartDateTextView.text.toString()
        val timeEstimate = mTimeEstimateEditText.text.toString().toLong()
        val item = hashMapOf(
                "assignmentName" to assignName,
                "importance" to imp,
                "dueTime" to dueTime,
                "dueDate" to dueDate,
                "startDate" to startDate,
                "timeEstimate" to timeEstimate,
                "isComplete" to false,
                "timeSpent" to 0
        )
        var classDocumentId = ""
        for (c in ClassSchedule.mClasses) {
            if (c.className == className) {
                classDocumentId = c.documentId!!
            }
        }
        if(FirebaseAuth.getInstance().currentUser != null){
            uid = FirebaseAuth.getInstance().currentUser!!.uid
        }
        db.collection("user").document(uid).collection("classes").document(classDocumentId).collection("assignments")
                .add(item)
                .addOnSuccessListener{DocumentReference->
                    item["className"] = className
                    AssignmentList.createAssignment(item, DocumentReference.id)
                    Log.d(TAG, "Added assignment with ${DocumentReference.id}")
                    Toast.makeText(activity!!,"Added Assignment",Toast.LENGTH_LONG).show()
                    activity!!.finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(activity!!,"Unable to add assignment",Toast.LENGTH_LONG).show()
                    Log.w(TAG, "Error adding document", e)
                    activity!!.finish()
                }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.due_time_button -> showTimePickerDialog("dueTime")
            R.id.due_date_button -> showDatePickerDialog("dueDate")
            R.id.start_date_button -> showDatePickerDialog("startDate")
            R.id.add_assign_button -> addToFirestore()
        }
    }
}
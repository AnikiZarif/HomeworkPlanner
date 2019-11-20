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

class AssignmentInfoFragment: Fragment(), View.OnClickListener {
    private var uid = ""
    private val db = FirebaseFirestore.getInstance()
    private lateinit var mClassNameInfoTextView: TextView
    private lateinit var mAssignmentNameTextView: TextView
    private lateinit var mAssignmentDueTimeInfoTextView: TextView
    private lateinit var mAssignmentDueDateInfoTextView: TextView
    private lateinit var mAssignmentStartDateTextView: TextView
    private lateinit var mAssignmentTimeEstimateTextView: TextView
    private lateinit var mAssignmentTimeSpentEditText: EditText
    private lateinit var mAssignmentImportanceInfoTextView: TextView
    private lateinit var mAssignmentCompletedSwitch: Switch
    private lateinit var mDeleteAssignmentButton: Button
    private lateinit var mEditAssignmentButton: Button

    private val TAG = AssignmentInfoFragment::class.java.simpleName

    companion object {
        fun newInstance(classInfo: Bundle): AssignmentInfoFragment {
            val fragment = AssignmentInfoFragment()

            fragment.arguments = classInfo

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):View? {
        val v: View
        val rotation = activity?.windowManager?.defaultDisplay?.rotation
        v = if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            // TODO: Create add class landscape fragment
            inflater.inflate(R.layout.fragment_assignment_info, container, false)
        } else {
            inflater.inflate(R.layout.fragment_assignment_info, container, false)
        }

        Log.d(TAG,"AssignmentInfoFragment invoked")
        mClassNameInfoTextView = v.findViewById(R.id.assignment_class_name_info)
        mAssignmentNameTextView = v.findViewById(R.id.assignment_name_info)
        mAssignmentDueTimeInfoTextView = v.findViewById(R.id.assignment_due_time_info)
        mAssignmentDueDateInfoTextView = v.findViewById(R.id.assignment_due_date_info)
        mAssignmentStartDateTextView = v.findViewById(R.id.assignment_start_date_info)
        mAssignmentTimeEstimateTextView = v.findViewById(R.id.assignment_time_estimate_info)
        mAssignmentTimeSpentEditText = v.findViewById(R.id.assign_actual_time_info_text)
        mAssignmentImportanceInfoTextView = v.findViewById(R.id.assignment_importance_info)
        mAssignmentCompletedSwitch = v.findViewById(R.id.assign_completed_switch)

        mClassNameInfoTextView.text = arguments?.getString("className")
        mAssignmentNameTextView.text = arguments?.getString("assignmentName")
        mAssignmentDueTimeInfoTextView.text = arguments?.getString("dueTime")
        mAssignmentDueDateInfoTextView.text = arguments?.getString("dueDate")
        mAssignmentStartDateTextView.text = arguments?.getString("startDate")
        mAssignmentTimeEstimateTextView.text = arguments?.getLong("timeEstimate").toString()
        mAssignmentTimeSpentEditText.setText(arguments?.getLong("timeSpent")!!.toString())
        mAssignmentImportanceInfoTextView.text = arguments?.getString("importance")
        mAssignmentCompletedSwitch.isChecked = false
        if (arguments?.getBoolean("completed")!!) {
            mAssignmentCompletedSwitch.isChecked = true
        }

        mDeleteAssignmentButton = v.findViewById(R.id.delete_assign_button2)
        mDeleteAssignmentButton.setOnClickListener(this)

        mEditAssignmentButton = v.findViewById(R.id.edit_assign_button)
        mEditAssignmentButton.setOnClickListener(this)

        return v
    }

    fun editAssignment() {
        val timeSpent = mAssignmentTimeSpentEditText.text.toString().toLong()
        val completed = mAssignmentCompletedSwitch.isChecked
        if (timeSpent == arguments?.getLong("timeSpent") &&
                completed == arguments?.getBoolean("completed")) {
            activity!!.finish()
            return
        }
        val dataMap = hashMapOf(
                "timeSpent" to timeSpent,
                "isComplete" to completed
        )
        if(FirebaseAuth.getInstance().currentUser != null){
            uid = FirebaseAuth.getInstance().currentUser!!.uid
            Log.d(TAG,"UID = $uid")
        }
        var classDocumentId = ""
        for (c in ClassSchedule.mClasses) {
            if (c.className == mClassNameInfoTextView.text.toString()) {
                classDocumentId = c.documentId!!
            }
        }
        db.collection("user").document(uid)
                .collection("classes").document(classDocumentId)
                .collection("assignments").document(arguments?.getString("documentId")!!)
                .update(dataMap)
                .addOnSuccessListener {
                    Toast.makeText(activity!!,"Updated Assignment", Toast.LENGTH_LONG).show()
                    for (a in AssignmentList.mAssignments) {
                        if (a.name == mAssignmentNameTextView.text.toString()) {
                            a.actualTimeSpent = timeSpent
                            a.isComplete = completed
                            break
                        }
                    }
                    activity!!.finish()
                }
    }

    fun deleteAssignment(){
        val className = mClassNameInfoTextView.text.toString()
        val assignmentName = mAssignmentNameTextView.text.toString()
        if(FirebaseAuth.getInstance().currentUser != null){
            uid = FirebaseAuth.getInstance().currentUser!!.uid
            Log.d(TAG,"UID = $uid")
        }
        var classDocumentId = ""
        for (c in ClassSchedule.mClasses) {
            if (c.className == className) {
                classDocumentId = c.documentId!!
            }
        }
        db.collection("user").document(uid).collection("classes").document(classDocumentId).collection("assignments")
                .whereEqualTo("assignmentName", assignmentName)
                .get()
                .addOnSuccessListener {documents ->
                    for (document in documents) {
                        document.reference.delete()
                    }
                    val listIter = AssignmentList.mAssignments.listIterator()
                    while (listIter.hasNext()) {
                        if (listIter.next().name == assignmentName) {
                            listIter.remove()
                        }
                    }
                    activity!!.finish()
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
    }
    
    override fun onClick(v: View) {
        when (v.id) {
            R.id.delete_assign_button2 -> deleteAssignment()
            R.id.edit_assign_button -> editAssignment()
        }
    }

}
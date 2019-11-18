package com.mobileappdev.homeworkplanner

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ClassInfoFragment: Fragment(), View.OnClickListener {
    private lateinit var mClassNameTextView: TextView
    private lateinit var mClassStartTimeInfoTextView: TextView
    private lateinit var mClassEndTimeInfoTextView: TextView
    private lateinit var mClassDaysInfoTextView: TextView
    private lateinit var mClassCreditsInfoTextView: TextView
    private lateinit var mDeleteClassButton: Button

    private val TAG = ClassInfoFragment::class.java.simpleName

    companion object {
        fun newInstance(classInfo: Bundle): ClassInfoFragment {
            val fragment = ClassInfoFragment()

            fragment.arguments = classInfo

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):View? {
        val v: View
        val rotation = activity?.windowManager?.defaultDisplay?.rotation
        v = if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            // TODO: Create add class landscape fragment
            inflater.inflate(R.layout.fragment_class_info, container, false)
        } else {
            inflater.inflate(R.layout.fragment_class_info, container, false)
        }

        Log.d(TAG,"ClassInfoFragment invoked")
        mClassNameTextView = v.findViewById(R.id.class_name_info)
        mClassStartTimeInfoTextView = v.findViewById(R.id.class_start_time_info)
        mClassEndTimeInfoTextView = v.findViewById(R.id.class_end_time_info)
        mClassDaysInfoTextView = v.findViewById(R.id.class_days_info)
        mClassCreditsInfoTextView = v.findViewById(R.id.class_credits_info)

        mClassNameTextView.text = arguments?.getString("className")
        mClassStartTimeInfoTextView.text = arguments?.getString("startTime")
        mClassEndTimeInfoTextView.text = arguments?.getString("endTime")
        mClassCreditsInfoTextView.text = arguments?.getLong("creditHours").toString()

        arguments?.getStringArrayList("classDays")?.forEach {
            if (mClassDaysInfoTextView.text.isEmpty()) {
                mClassDaysInfoTextView.text = it
            } else {
                mClassDaysInfoTextView.text = getString(R.string.day_list, mClassDaysInfoTextView.text, it)
            }
        }

        mDeleteClassButton = v.findViewById(R.id.delete_class_button2)
        mDeleteClassButton.setOnClickListener(this)

        return v
    }

    fun deleteClass(){
        val text = mClassNameTextView.text.toString()
        val db = FirebaseFirestore.getInstance()
        var uid = ""
        if(FirebaseAuth.getInstance().currentUser != null){
            uid = FirebaseAuth.getInstance().currentUser!!.uid
            Log.d(TAG,"UID = $uid")
        }
        db.collection("user").document(uid).collection("classes")
                .whereEqualTo("className", text)
                .get()
                .addOnSuccessListener {documents ->
                    for (document in documents) {
                        document.reference.delete()
                    }
                    activity!!.finish()
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
    }
    
    override fun onClick(v: View) {
        when (v.id) {
            R.id.delete_class_button2 -> deleteClass()
        }
    }

}
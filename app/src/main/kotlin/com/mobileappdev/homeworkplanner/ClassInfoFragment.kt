package com.mobileappdev.homeworkplanner

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.R

class ClassInfoFragment: Fragment(), View.OnClickListener {
    private lateinit var mClassNameTextView: TextView
    private lateinit var mClassNameInfoTextView: TextView

    private val TAG = ClassInfoFragment::class.java.simpleName

    companion object {
        const val CLASS_NAME = "Class_Name"

        fun newInstance(className: String): ClassInfoFragment {
            val fragment = ClassInfoFragment()

            val bundle = Bundle().apply {
                putString(CLASS_NAME, className)
            }

            fragment.arguments = bundle

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
        mClassNameInfoTextView = v.findViewById(R.id.class_name_text_info)

        val name = arguments?.getString(CLASS_NAME)
        mClassNameInfoTextView.text = name
        mClassNameTextView.text = name

        return v
    }

    
    override fun onClick(v: View) {
        when (v.id) {
        }
    }

}
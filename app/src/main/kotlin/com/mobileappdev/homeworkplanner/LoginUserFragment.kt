package com.mobileappdev.homeworkplanner

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.myapplication.R

class LoginUserFragment: Fragment(), View.OnClickListener {
    private val TAG = LoginUserFragment::class.java.simpleName
    private lateinit var mUserNameText : EditText
    private lateinit var mPasswordText : EditText
    private lateinit var mResetButton : Button
    private lateinit var mSubmitButton : Button
    private lateinit var mNewUserButton : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):View? {
        val v: View
        val rotation = activity?.windowManager?.defaultDisplay?.rotation
        v = if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            // TODO: Create add class landscape fragment
            inflater.inflate(R.layout.user_login, container, false)
        } else {
            inflater.inflate(R.layout.user_login, container, false)
        }

        mUserNameText = v.findViewById(R.id.user_name)

        mPasswordText = v.findViewById(R.id.password)

        mResetButton = v.findViewById(R.id.reset)
        mResetButton.setOnClickListener(this)

        mSubmitButton = v.findViewById(R.id.submit)
        mSubmitButton.setOnClickListener(this)

        mNewUserButton = v.findViewById(R.id.new_user)
        mNewUserButton.setOnClickListener(this)
        Log.d(TAG, "Login page created")

        return v
    }


    fun resetText(){
        mUserNameText.setText("")
        mPasswordText.setText("")
    }

    fun openFragment(){
//        val fm = supportFragmentManager
//        var fragment = fm.findFragmentById(R.id.fragment_container)
//
//        Log.d(TAG,"HERE + ${fragment}")
//        if (fragment == null) {
//            fragment = AddClassFragment()
//            Log.d(TAG,"HEREyyyyyyyyyy")
//            fm.beginTransaction()
//                    .add(R.id.fragment_container, fragment)
//                    .commit()
//        }
        startActivity(Intent(activity, AddClassActivity::class.java))
        activity?.finish()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.reset -> resetText()
            R.id.new_user -> null
            R.id.submit -> openFragment()
        }
    }

}
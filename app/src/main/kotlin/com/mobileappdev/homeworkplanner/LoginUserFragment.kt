package com.mobileappdev.homeworkplanner

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth

class LoginUserFragment: Fragment(), View.OnClickListener {
    private val TAG = LoginUserFragment::class.java.simpleName
    private lateinit var mUserNameText : EditText
    private lateinit var mPasswordText : EditText
    private lateinit var mResetButton : Button
    private lateinit var mSubmitButton : Button
    private lateinit var mNewUserButton : Button
    private lateinit var mEnlargeButton: Button

    private var mAuth = FirebaseAuth.getInstance()

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

        mEnlargeButton = v.findViewById(R.id.enlarge_font)
        mEnlargeButton.setOnClickListener(this)

        return v
    }


    fun resetText(){
        mUserNameText.setText("")
        mPasswordText.setText("")
    }

    fun openFragment(){
        val email = mUserNameText.text.toString()
        val password = mPasswordText.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(activity!!, "Please fill in email and password", Toast.LENGTH_LONG).show()
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(activity!!, "Please enter a valid email address", Toast.LENGTH_LONG).show()
            mUserNameText.setText("")
            mPasswordText.setText("")
        }else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity!!) { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Open Fragment")
                            startActivity(Intent(activity, ClassListActivity::class.java))
                            activity!!.finish()
                        } else {
                            Log.d(TAG, "ERROR: Logging in the user")
                            Toast.makeText(activity!!, "Unable to Login", Toast.LENGTH_LONG).show()
                        }
                    }
        }
    }

    fun createUser(){
        Log.d(TAG, "Create User")
        startActivity(Intent(activity, AddUserActivity::class.java))
    }

    fun enlargeFont(){
        if(mEnlargeButton.text == "Smaller Font"){
            mEnlargeButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (14.0).toFloat())
            mEnlargeButton.setText("Larger Font")
            mResetButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (14.0).toFloat())
            mSubmitButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (14.0).toFloat())
            mNewUserButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (14.0).toFloat())
            mUserNameText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (14.0).toFloat())
            mResetButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (14.0).toFloat())
        }else{
            mEnlargeButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (30.0).toFloat())
            mEnlargeButton.setText("Smaller Font")
            mResetButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (30.0).toFloat())
            mSubmitButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (30.0).toFloat())
            mNewUserButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (30.0).toFloat())
            mUserNameText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (30.0).toFloat())
            mResetButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (30.0).toFloat())
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.reset -> resetText()
            R.id.new_user -> createUser()
            R.id.submit -> openFragment()
            R.id.enlarge_font-> enlargeFont()
        }
    }

}
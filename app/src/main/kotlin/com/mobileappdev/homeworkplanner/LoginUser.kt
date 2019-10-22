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
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

class LoginUser: AppCompatActivity(){
    private val TAG = LoginUser::class.java!!.getSimpleName()
    private lateinit var mUserNameText : EditText
    private lateinit var mPasswordText : EditText
    private lateinit var mResetButton : Button
    private lateinit var mSubmitButton : Button
    private lateinit var mNewUserButton : Button

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_login)


        val clickListener = View.OnClickListener {view ->
            when (view.getId()) {
                R.id.reset -> resetText()
                R.id.new_user -> openFragment()
                R.id.submit -> null
            }
        }

        mUserNameText = findViewById(R.id.user_name)

        mPasswordText = findViewById(R.id.password)

        mResetButton = findViewById(R.id.reset)
        mResetButton.setOnClickListener(clickListener)

        mSubmitButton = findViewById(R.id.submit)
        mSubmitButton.setOnClickListener(clickListener)

        mNewUserButton = findViewById(R.id.new_user)
        mNewUserButton.setOnClickListener(clickListener)
        Log.d(TAG, "Login page created")

    }


    fun resetText(){
        mUserNameText.setText("")
        mPasswordText.setText("")
    }

    fun openFragment(){
        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fragment_container)

        Log.d(TAG,"HERE + ${fragment}")
        if (fragment == null) {
            fragment = AddClassFragment()
            Log.d(TAG,"HEREyyyyyyyyyy")
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit()
        }
    }



}
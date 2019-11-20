package com.mobileappdev.homeworkplanner

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.widget.Constraints

import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ClassListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)

        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fragment_container)

        if (fragment == null) {
            fragment = ClassListFragment()
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit()
            Log.d("lifecycle", "onCreate invoked")
        }
    }

}
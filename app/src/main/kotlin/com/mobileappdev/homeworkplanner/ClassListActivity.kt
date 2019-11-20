package com.mobileappdev.homeworkplanner


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.R


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
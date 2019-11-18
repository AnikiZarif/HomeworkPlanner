package com.mobileappdev.homeworkplanner

import android.os.Bundle
import android.util.Log
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class ClassInfoActivity: AppCompatActivity() {

    @LayoutRes
    private fun getLayoutResId(): Int {
        return R.layout.activity_fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())

        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fragment_container)

        if (fragment == null) {
            val classInfo = intent.extras!!.getBundle("classInfo")!!
            fragment = ClassInfoFragment.newInstance(classInfo)
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit()
        }
        Log.d("lifecycle", "onCreate invoked")
    }
}
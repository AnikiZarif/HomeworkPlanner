package com.mobileappdev.homeworkplanner

import android.content.Intent
import android.os.Handler
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class MainActivity: AppCompatActivity() {

    private val TAG = MainActivity::class.java!!.getSimpleName()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val handler = android.os.Handler()
        val intent = Intent(this, LoginUser::class.java!!);

        handler.postDelayed({
            Log.d(TAG, "HEREy ${intent}");
            startActivity(intent);
            finish();
            },3000)
        }
    }

    /*
    override fun onClickView(view){
        R.id.new_user ->
    }
            /*
        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fragment_container)

        if (fragment == null) {
            fragment = AddClassFragment()
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit()
        }
        Log.d("lifecycle", "onCreate invoked kotlin")

         */
        //final val mNewUser : Button = findViewById(R.id.new_user)
        //mNewUser.setOnClickListener(this)


     */
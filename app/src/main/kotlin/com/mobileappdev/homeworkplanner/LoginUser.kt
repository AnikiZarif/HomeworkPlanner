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

    override fun onCreate(savedInstanceState: Bundle?){
        Log.d(TAG,"HERE OOOOOOOOOOOO")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_login)
    }

}
package com.mobileappdev.homeworkplanner

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.regex.Pattern
import com.google.firebase.firestore.FirebaseFirestore



class AddUserFragment: Fragment(), View.OnClickListener {
    private lateinit var mUserNameEditText: EditText
    private lateinit var mUserEmailEditText: EditText
    private lateinit var mUserPasswordEditText: EditText
    private lateinit var mUserRepeatPasswordEditText: EditText
    private lateinit var mYearSpinner: Spinner
    private lateinit var mSubmitButton: Button

    //Firebase references
    private val mFirestore = FirebaseFirestore.getInstance()
    private var mAuth = FirebaseAuth.getInstance()

    private val TAG = AddUserFragment::class.java!!.getSimpleName()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View

        val rotation = activity?.windowManager?.defaultDisplay?.rotation
        v = if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            // TODO: Create add class landscape fragment
            inflater.inflate(R.layout.new_user, container, false)
        } else {
            inflater.inflate(R.layout.new_user, container, false)
        }

        Log.d(TAG,"Adding user fragment created")

        mYearSpinner = v.findViewById(R.id.year_spinner)

        val years = arrayOf("Freshman", "Sophomore", "Junior", "Senior")
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, years)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mYearSpinner.adapter = adapter

        mUserNameEditText = v.findViewById(R.id.user_name)

        mUserEmailEditText = v.findViewById(R.id.user_email)

        mUserPasswordEditText = v.findViewById(R.id.user_password)

        mUserRepeatPasswordEditText = v.findViewById(R.id.repeat_password)

        mSubmitButton = v.findViewById(R.id.submit_button)
        mSubmitButton.setOnClickListener(this)

        return v
    }

    fun submit(){
        var name = mUserNameEditText.text.toString()
        var email = mUserEmailEditText.text.toString()
        var password = mUserPasswordEditText.text.toString()
        var repeat = mUserRepeatPasswordEditText.text.toString()
        var year = mYearSpinner.selectedItem.toString()

        if(!password.equals(repeat)){
            Toast.makeText(activity!!, "Password is not repeated correctly", Toast.LENGTH_SHORT).show()
            mUserPasswordEditText.setText("")
            mUserRepeatPasswordEditText.setText("")
            return
        }
        val item = hashMapOf(
                "name" to name,
                "email" to email,
                "year" to year
        )
        val regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!-]")

        if(password.length < 6){
            Toast.makeText(activity!!, "Password is not long enough", Toast.LENGTH_LONG).show()
            mUserPasswordEditText.setText("")
            mUserRepeatPasswordEditText.setText("")
            return
        }

        if (!regex.matcher(password).find()) {
            Toast.makeText(activity!!, "Password must include special character", Toast.LENGTH_LONG).show()
            mUserPasswordEditText.setText("")
            mUserRepeatPasswordEditText.setText("")
            return
        }
        var lowCase = 0
        var upCase = 0

        for(letter in password){
            if(letter.isLowerCase()){
                lowCase++
            }else if(letter.isUpperCase()){
                upCase++
            }
        }
        if(lowCase == 0 || upCase == 0){
            Toast.makeText(activity!!, "Password must include at least one upper or lower case", Toast.LENGTH_LONG).show()
            mUserPasswordEditText.setText("")
            mUserRepeatPasswordEditText.setText("")
            return
        }

        if(email.equals("") || password.equals("")){
            Toast.makeText(activity!!, "Please fill in email and password", Toast.LENGTH_LONG).show()
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(activity!!, "Please enter a valid email address", Toast.LENGTH_LONG).show()
            mUserNameEditText.setText("")
            mUserEmailEditText.setText("")
            mUserPasswordEditText.setText("")
            mUserRepeatPasswordEditText.setText("")
        }else {
            mAuth!!.createUserWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener(activity!!) { task ->
                        if (task.isSuccessful) {
                            val uid = mAuth!!.currentUser!!.uid
                            sendEmail()
                            mFirestore.collection("user").document(uid).set(item)
                        } else {
                            Toast.makeText(activity!!, "Failed to create user ", Toast.LENGTH_LONG).show()
                        }
                    }
        }

        Log.d(TAG,"Adding user")
    }

    fun sendEmail(){
        val mEmail = mAuth!!.currentUser
        mEmail!!.sendEmailVerification()
                .addOnCompleteListener(activity!!){task ->
                    if(task.isSuccessful){
                        Toast.makeText(activity!!, "Sent email", Toast.LENGTH_LONG).show()
                        startActivity(Intent(activity!!,ClassListActivity::class.java))
                    }else {
                        Toast.makeText(activity!!, "Failed to send email", Toast.LENGTH_LONG).show()
                        Log.d(TAG, "Failed to send email")
                    }
        }

    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.submit_button -> submit()
        }
    }
}
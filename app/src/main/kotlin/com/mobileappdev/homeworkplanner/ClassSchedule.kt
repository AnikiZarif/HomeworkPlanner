package com.mobileappdev.homeworkplanner

import android.util.Log
import androidx.annotation.NonNull
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList
import androidx.constraintlayout.widget.Constraints.TAG
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.*

object ClassSchedule {
    private var uid = ""

    var mClasses: ArrayList<Class> = ArrayList()

    private fun createClass(dataMap: Map<String, Any>) {
        val aClass = Class()
        aClass.daysOfWeek = dataMap["dayOfWeek"] as ArrayList<String>?
        aClass.className = dataMap["className"] as String?
        aClass.startTime = dataMap["classStartTime"] as String?
        aClass.endTime = dataMap["classEndTime"] as String?
        aClass.creditHours = dataMap["creditHours"] as Long
        mClasses.add(aClass)
    }

    fun initRoutine() = runBlocking {
        val db = FirebaseFirestore.getInstance()

        // TODO: Populate with classes from database
        if (FirebaseAuth.getInstance().currentUser != null) {
            uid = FirebaseAuth.getInstance().currentUser!!.uid
            Log.d("uid:  ", uid)
        }

        //db.collection("user").document(uid).
        Tasks.await(db.collection("user").document(uid).collection("classes")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            Log.d(TAG, document.id + " => " + document.data)
                            createClass(document.data)
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.exception)
                    }
                })
    }

    fun addClass(className: String, startTime: String, endTime: String, daysWeek: ArrayList<String>,
                 creditHours: Int) {
        val aClass = Class()
        aClass.className = className
        aClass.startTime = startTime
        aClass.endTime = endTime
        aClass.daysOfWeek = daysWeek
    }
}

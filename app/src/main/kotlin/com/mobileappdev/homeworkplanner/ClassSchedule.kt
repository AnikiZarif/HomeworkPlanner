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

    fun createClass(dataMap: Map<String, Any>) {
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

        if (FirebaseAuth.getInstance().currentUser != null) {
            uid = FirebaseAuth.getInstance().currentUser!!.uid
            Log.d("uid:  ", uid)
        }

        mClasses.clear()
        //db.collection("user").document(uid).
        Tasks.await(db.collection("user").document(uid).collection("classes")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            document.reference.collection("assignments")
                                    .get()
                                    .addOnCompleteListener { task2 ->
                                        for (assignment in task2.result!!) {
                                            val dataMap = assignment.data
                                            dataMap!!["className"] = document.data["className"]
                                            AssignmentList.createAssignment(dataMap)
                                        }
                                    }
                            Log.d(TAG, document.id + " => " + document.data)
                            createClass(document.data)
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.exception)
                    }
                })
    }
}

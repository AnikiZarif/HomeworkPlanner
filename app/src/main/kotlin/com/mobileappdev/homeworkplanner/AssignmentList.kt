package com.mobileappdev.homeworkplanner

import java.util.ArrayList

object AssignmentList {
    var mAssignments: ArrayList<Assignment> = ArrayList()

    fun createAssignment(dataMap: Map<String, Any>) {
        val assignment = Assignment()
        assignment.name = dataMap["assignmentName"] as String?
        assignment.dueDate = dataMap["dueDate"] as String?
        assignment.dueTime = dataMap["dueTime"] as String?
        assignment.importance = dataMap["importance"] as String?
        assignment.parentClass = dataMap["className"] as String?
        assignment.startDate = dataMap["startDate"] as String?
        assignment.timeEstimate = dataMap["timeEstimate"] as Int?
        mAssignments.add(assignment)
    }
}

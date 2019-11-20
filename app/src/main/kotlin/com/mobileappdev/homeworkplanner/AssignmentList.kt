package com.mobileappdev.homeworkplanner

import java.util.ArrayList

object AssignmentList {
    var mAssignments: ArrayList<Assignment> = ArrayList()

    fun createAssignment(dataMap: Map<String, Any>, documentId: String) {
        val assignment = Assignment()
        assignment.name = dataMap["assignmentName"] as String?
        assignment.dueDate = dataMap["dueDate"] as String?
        assignment.dueTime = dataMap["dueTime"] as String?
        assignment.importance = dataMap["importance"] as String?
        assignment.parentClass = dataMap["className"] as String?
        assignment.startDate = dataMap["startDate"] as String?
        assignment.timeEstimate = dataMap["timeEstimate"] as Long
        assignment.actualTimeSpent = dataMap["timeSpent"] as Long
        assignment.isComplete = dataMap["isComplete"] as Boolean?
        assignment.documentId = documentId
        mAssignments.add(assignment)
    }
}

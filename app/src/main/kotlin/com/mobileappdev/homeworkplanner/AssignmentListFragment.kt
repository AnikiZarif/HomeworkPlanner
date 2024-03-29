package com.mobileappdev.homeworkplanner

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.myapplication.R
import kotlinx.coroutines.runBlocking

class AssignmentListFragment : Fragment() {

    private var mAssignmentRecyclerView: RecyclerView? = null
    private var mAdapter: AssignmentAdapter? = null
    private var mAddAssignmentButton: Button? = null
    private var mCalendarButton: Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_assignment_list, container, false)

        mAssignmentRecyclerView = view.findViewById<View>(R.id.assignment_recycler_view) as RecyclerView
        mAssignmentRecyclerView!!.layoutManager = LinearLayoutManager(activity)
        mAddAssignmentButton = view.findViewById<View>(R.id.add_assignment_button) as Button
        mAddAssignmentButton!!.setOnClickListener {
            val intent = Intent(activity, AddAssignActivity::class.java)
            startActivity(intent)
        }

        mCalendarButton = view.findViewById(R.id.view_calendar_button) as Button
        mCalendarButton!!.setOnClickListener({
            val intent = Intent(activity, PortraitCalendarActivity::class.java)
            startActivity(intent)
        })

        mAdapter = AssignmentAdapter(AssignmentList.mAssignments)
        mAssignmentRecyclerView!!.adapter = mAdapter

        return view
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private inner class AssignmentHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item_assignment, parent, false)), View.OnClickListener {
        private val mAssignmentTitleTextView: TextView
        private val mClassTitleTextView: TextView
        private var mAssignment: Assignment? = null


        override fun onClick(view: View) {
            val assignmentInfoAct = Intent(activity, AssignmentInfoActivity::class.java)
            val bundle = Bundle().apply {
                putString("className", mAssignment!!.parentClass)
                putString("assignmentName", mAssignment!!.name)
                putString("dueTime", mAssignment!!.dueTime)
                putString("dueDate", mAssignment!!.dueDate)
                putString("importance", mAssignment!!.importance)
                putString("startDate", mAssignment!!.startDate)
                putLong("timeEstimate", mAssignment!!.timeEstimate!!)
                putLong("timeSpent", mAssignment!!.actualTimeSpent!!)
                putBoolean("completed", mAssignment!!.isComplete!!)
                putString("documentId", mAssignment!!.documentId)
            }
            assignmentInfoAct.putExtra("assignmentInfo", bundle)
            startActivity(assignmentInfoAct)
        }

        init {
            itemView.setOnClickListener(this)

            mClassTitleTextView = itemView.findViewById(R.id.class_title)
            mAssignmentTitleTextView = itemView.findViewById<View>(R.id.assignment_title) as TextView
        }

        fun bind(assignment: Assignment) {
            mAssignment = assignment
            mAssignmentTitleTextView.text = mAssignment!!.name
            mClassTitleTextView.text = mAssignment!!.parentClass + ":"
        }
    }

    private fun updateUI() = runBlocking {
        activity!!.runOnUiThread {
            mAssignmentRecyclerView!!.adapter!!.notifyDataSetChanged()
        }
    }

    private inner class AssignmentAdapter(private val mAssignments: List<Assignment>) : RecyclerView.Adapter<AssignmentHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignmentHolder {
            val layoutInflater = LayoutInflater.from(activity)

            return AssignmentHolder(layoutInflater, parent)
        }

        override fun onBindViewHolder(holder: AssignmentHolder, position: Int) {
            val assignment = mAssignments[position]
            holder.bind(assignment)
        }

        override fun getItemCount(): Int {
            return mAssignments.size
        }
    }
}

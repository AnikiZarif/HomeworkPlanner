package com.mobileappdev.homeworkplanner

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ClassListFragment : Fragment() {

    private var mCrimeRecyclerView: RecyclerView? = null
    private var mAdapter: ClassAdapter? = null
    private var mAddClassButton: Button? = null
    private var mShowAssignmentsButton: Button? = null
    private var classScheduleInitialized = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_class_list, container, false)

        mCrimeRecyclerView = view.findViewById<View>(R.id.class_recycler_view) as RecyclerView
        mCrimeRecyclerView!!.layoutManager = LinearLayoutManager(activity)
        mAddClassButton = view.findViewById<View>(R.id.add_class_button) as Button
        mAddClassButton!!.setOnClickListener {
            val intent = Intent(activity, AddClassActivity::class.java)
            startActivity(intent)
        }

        mShowAssignmentsButton = view.findViewById(R.id.show_assignment_button)
        mShowAssignmentsButton!!.setOnClickListener {
            val intent = Intent(activity, AssignmentListActivity::class.java)
            startActivity(intent)
        }

        mAdapter = ClassAdapter(ClassSchedule.mClasses)
        mCrimeRecyclerView!!.adapter = mAdapter

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity, LoginUserActivity::class.java))
            activity!!.finish()
        }

        callback.isEnabled = true

        return view
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch {
            coroutineScope {
                updateUI()
            }
        }
    }

    private inner class ClassHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item_class, parent, false)), View.OnClickListener {
        private val mTitleTextView: TextView
        private var mClass: Class? = null


        override fun onClick(view: View) {
            val classInfoAct = Intent(activity, ClassInfoActivity::class.java)
            val bundle = Bundle().apply {
                putString("className", mClass!!.className)
                putString("startTime", mClass!!.startTime)
                putString("endTime", mClass!!.endTime)
                putLong("creditHours", mClass!!.creditHours)
                putStringArrayList("classDays", mClass!!.daysOfWeek)
            }
            classInfoAct.putExtra("classInfo", bundle)
            startActivity(classInfoAct)
        }

        init {
            itemView.setOnClickListener(this)

            mTitleTextView = itemView.findViewById<View>(R.id.class_title) as TextView
        }

        fun bind(aClass: Class) {
            mClass = aClass
            mTitleTextView.text = mClass!!.className
        }
    }

    private fun updateUI() = runBlocking {
        if (!classScheduleInitialized) {
            val lol = GlobalScope.launch { ClassSchedule.initRoutine() }
            lol.join()
            classScheduleInitialized = true
        }
        activity!!.runOnUiThread {
            mCrimeRecyclerView!!.adapter!!.notifyDataSetChanged()
        }
    }

    private inner class ClassAdapter(private val mClasses: List<Class>) : RecyclerView.Adapter<ClassHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassHolder {
            val layoutInflater = LayoutInflater.from(activity)

            return ClassHolder(layoutInflater, parent)
        }

        override fun onBindViewHolder(holder: ClassHolder, position: Int) {
            val aClass = mClasses[position]
            holder.bind(aClass)
        }

        override fun getItemCount(): Int {
            return mClasses.size
        }
    }
}

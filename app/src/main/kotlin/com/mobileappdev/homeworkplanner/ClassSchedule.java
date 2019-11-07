package com.mobileappdev.homeworkplanner;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ClassSchedule {
    private static ClassSchedule sClassSchedule;
    private String uid = "";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private List<Class> mClasses;

    public static ClassSchedule get(Context context){
        if (sClassSchedule == null){
            sClassSchedule = new ClassSchedule(context);
        }
        return sClassSchedule;
    }

    private void createClass(Map<String, Object> dataMap) {
        Class aClass = new Class();
        aClass.setDaysOfWeek((ArrayList<String>) dataMap.get("dayOfWeek"));
        aClass.setTitile((String) dataMap.get("className"));
        aClass.setTimeOfDay((String) dataMap.get("classStartTime"));
        mClasses.add(aClass);
    }

    private ClassSchedule(Context context){
        mClasses = new ArrayList<>();

        // TODO: Populate with classes from database
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        db.collection("user").document(uid).collection("classes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                createClass(document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public List<Class> getClasses(){ return mClasses; }
}

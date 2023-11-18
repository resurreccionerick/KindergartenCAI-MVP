package com.example.myapplication.Student.Subject

import com.example.myapplication.Models.Subject
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SubjectPresenter(private var view: SubjectActivity) : SubjectContract.Presenter {

    val databaseReference = FirebaseDatabase.getInstance().getReference("Subjects")


    override fun loadSubjects() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val subjects = mutableListOf<Subject>()
                for (childSnapshot in dataSnapshot.children) {
                    val id = childSnapshot.child("id").value.toString()
                    val title = childSnapshot.child("title").value.toString()
                    val imageUrl = childSnapshot.child("imageUrl").value.toString()
                    val subject = Subject(id, title, imageUrl)
                    subjects.add(subject)
                }
                view.showSubjects(subjects)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                view.showErrorMessage("Error: " + databaseError.message)
            }
        })
    }


}
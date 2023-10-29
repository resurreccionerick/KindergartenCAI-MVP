package com.example.myapplication.Teacher.Subject

import com.example.myapplication.Models.Subject
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SubjectPresenter(private val view: SubjectContract.View) : SubjectContract.Presenter {

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


    override fun deleteSubject(subject: Subject) {
        // Get the unique ID of the subject and remove it from the database
        val subjectId = subject.id
        if (subjectId != null) {
            val subjectRef = databaseReference.child(subjectId)
            subjectRef.removeValue()
                .addOnSuccessListener {
                    view.showErrorMessage("Subject deleted: ${subject.title}")
                }
                .addOnFailureListener { exception ->
                    view.showErrorMessage("Error deleting subject: ${exception.message}")
                }
        }
    }
}

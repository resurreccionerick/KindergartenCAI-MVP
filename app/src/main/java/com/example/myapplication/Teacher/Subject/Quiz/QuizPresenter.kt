package com.example.myapplication.Teacher.Subject.Quiz

import com.example.myapplication.Models.Quiz
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class QuizPresenter(val subjectId: String, val view: TeacherQuizActivity) : QuizContract.Presenter {

    private val databaseReference =
        FirebaseDatabase.getInstance().getReference("Subjects").child(
            subjectId
        ).child("Quiz")

    override fun loadQuiz() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val quiz = mutableListOf<Quiz>()
                for (childSnapshot in dataSnapshot.children) {
                    val id = childSnapshot.child("id").value.toString()
                    val title = childSnapshot.child("title").value.toString()
                    val opt1 = childSnapshot.child("opt1").value.toString()
                    val opt2 = childSnapshot.child("opt2").value.toString()
                    val opt3 = childSnapshot.child("opt3").value.toString()
                    val opt4 = childSnapshot.child("opt4").value.toString()
                    val imageUrl = childSnapshot.child("titleImgUrl").value.toString()
                    val quizModel = Quiz(id, title, opt1, opt2, opt3, opt4, imageUrl)
                    quiz.add(quizModel)
                }
                view.showQuiz(quiz)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                view.showMessage("Error: " + databaseError.message)
            }
        })
    }

    override fun deleteQuiz(quiz: Quiz) {
        databaseReference.child(quiz.id.toString()).removeValue().addOnCompleteListener {
            view.showMessage("Quiz successfully deleted")
        }.addOnFailureListener {
            view.showMessage("Error: " + it.message)
        }
    }

}
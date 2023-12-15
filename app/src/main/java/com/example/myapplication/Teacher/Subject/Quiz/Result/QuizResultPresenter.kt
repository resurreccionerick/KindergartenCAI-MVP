package com.example.myapplication.Teacher.Subject.Quiz.Result

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class QuizResultPresenter(var view: QuizResultActivity) : QuizResultContract.Presenter {
    private val auth = FirebaseAuth.getInstance()
    private val databaseReference =
        FirebaseDatabase.getInstance().getReference("Users").child(
            auth.currentUser!!.uid
        )

    override fun saveQuizOnDatabase(newScore: String) {
        // Retrieve the current score from the database
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val currentScore = dataSnapshot.child("score").getValue(String::class.java)

                    // Add the new score to the current score
                    val totalScore = (currentScore?.toInt() ?: 0) + newScore.toInt()

                    // Update the value in the database including the name
                    val scoreMap = HashMap<String, Any>()
                    scoreMap["score"] = totalScore.toString()
                    scoreMap["email"] = auth.currentUser!!.email.toString()

                    databaseReference.setValue(scoreMap)
                        .addOnSuccessListener {
                            view.finishSave()
                            view.showMessage("Quiz result saved successfully!")
                        }
                        .addOnFailureListener {
                            view.showMessage("Failed to save quiz result. Please try again.")
                        }
                } else {
                    // If there is no existing score, set the new score directly
                    val scoreMap = HashMap<String, Any>()
                    scoreMap["score"] = newScore
                    scoreMap["email"] = auth.currentUser!!.email.toString()

                    databaseReference.setValue(scoreMap)
                        .addOnSuccessListener {
                            view.finishSave()
                            view.showMessage("Quiz result saved successfully!")
                        }
                        .addOnFailureListener {
                            view.showMessage("Failed to save quiz result. Please try again.")
                        }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
                view.showMessage("Error retrieving current score. Please try again.")
            }
        })
    }
}


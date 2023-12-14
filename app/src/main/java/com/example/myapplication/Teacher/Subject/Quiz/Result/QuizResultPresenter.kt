package com.example.myapplication.Teacher.Subject.Quiz.Result

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class QuizResultPresenter(var view: QuizResultActivity) : QuizResultContract.Presenter {
    private val auth = FirebaseAuth.getInstance()
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    override fun saveQuizOnDatabase(score: String) {
        if (score.isNotEmpty()) {
            // Use the current user's UID as the document ID
            val userDocument =
                firebaseFirestore.collection("Users").document(auth.currentUser!!.uid)

            userDocument.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // Get the current quizResults value from the document
                        val quizResults = documentSnapshot.get("quizResults")

                        // Update the quizResults value with the new score
                        val newResult = (quizResults as? Number)?.toLong()?.plus(score.toInt()) ?: 0

                        // Update the "quizResults" field within the user's document
                        userDocument.update("quizResults", newResult)
                            .addOnSuccessListener {
                                view.finishSave()
                                view.showMessage("Quiz result saved successfully!")
                            }
                            .addOnFailureListener {
                                view.showMessage("Failed to save quiz result. Please try again.")
                            }
                    } else {
                        view.showMessage("User document not found")
                    }
                }
                .addOnFailureListener {
                    view.showMessage("Failed to fetch user document. Please try again.")
                }
        } else {
            view.showMessage("Score is empty.")
        }
    }
}

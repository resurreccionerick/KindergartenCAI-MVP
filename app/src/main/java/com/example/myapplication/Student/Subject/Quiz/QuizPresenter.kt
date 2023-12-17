package com.example.myapplication.Student.Subject.Quiz

import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.example.myapplication.Models.Question
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class QuizPresenter(val view: QuizActivity, subjId: String) : QuizContract.Presenter {
    private val databaseReference =
        FirebaseDatabase.getInstance().reference.child("Subjects").child(subjId).child("Quiz")
    private val quizList = mutableListOf<Question>()
    private var currentQuestionIndex = 0
    private var score: Int = 0

    init {
        // Fetch quiz questions from Firebase
        fetchQuizQuestions(object : QuizCallback {
            override fun onQuizFetched() {
                loadQuizQuestion()
            }
        })
    }

    private fun fetchQuizQuestions(callback: QuizCallback) {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val questions = mutableListOf<Question>()
                for (snapshot in dataSnapshot.children) {
                    val title = snapshot.child("title").getValue(String::class.java) ?: ""
                    val imageUrl = snapshot.child("titleImgUrl").getValue(String::class.java) ?: ""
                    val option1 = snapshot.child("opt1").getValue(String::class.java) ?: ""
                    val option2 = snapshot.child("opt2").getValue(String::class.java) ?: ""
                    val option3 = snapshot.child("opt3").getValue(String::class.java) ?: ""
                    val option4 = snapshot.child("opt4").getValue(String::class.java) ?: ""
                    val correctOption =
                        (snapshot.child("correctAns").getValue(String::class.java) ?: "").toInt()

                    questions.add(
                        Question(
                            title,
                            imageUrl,
                            option1,
                            option2,
                            option3,
                            option4,
                            correctOption
                        )
                    )
                }

                if (questions.isNotEmpty()) {
                    quizList.addAll(questions)
                    callback.onQuizFetched() // Notify that questions are fetched
                } else {
                    view.finish()
                    view.showMessage("No Quiz Yet")
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                view.showMessage(databaseError.message)
            }
        })
    }

    override fun loadQuizQuestion() {
        Handler().postDelayed({
            if (currentQuestionIndex < quizList.size) {
                view.showQuizQuestion(
                    quizList[currentQuestionIndex],
                    currentQuestionIndex + 1,
                    quizList.size
                )
            }
        }, 500) // delay time
    }

    override fun checkAnswer(selectedOption: Int) {
        val currentQuestion = quizList[currentQuestionIndex]
        val isCorrect = selectedOption == currentQuestion.correctOption
        if (isCorrect) {
            score++
        }
        view.showQuizResult(isCorrect, score)

        // Move to the next question
        currentQuestionIndex++

        // Check if all questions are answered
        if (currentQuestionIndex < quizList.size) {
            loadQuizQuestion()
        } else {
            //view.showMessage("Quiz completed!")
            view.finishQuiz(score.toString()) // Notify the activity to finish
        }
    }

    interface QuizCallback {
        fun onQuizFetched()
    }
}
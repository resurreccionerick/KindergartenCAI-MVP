package com.example.myapplication.Teacher.Subject.Quiz

import com.example.myapplication.Models.Quiz

interface QuizContract {
    interface View {
        fun showQuiz(subjects: List<Quiz>)
        fun showMessage(message: String)
    }

    interface Presenter {
        fun loadQuiz()
        fun deleteQuiz(quiz: Quiz)

    }
}
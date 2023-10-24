package com.example.myapplication.Teacher.Quiz

interface QuizContract {
    interface View {

    }

    interface presenter {
        fun checkAnswer()

        fun showNextQuestion()

    }
}
package com.example.myapplication.Teacher.Subject.Quiz.Result

interface QuizResultContract {
    interface View{
        fun finishSave()
        fun showMessage(message: String)
    }

    interface Presenter{
        fun saveQuizOnDatabase(score: String);
    }
}
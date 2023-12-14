package com.example.myapplication.Student.Subject.Quiz

import com.example.myapplication.Models.Question

interface QuizContract {
    interface View {
        fun showQuizQuestion(question: Question, currentQuestionIndex: Int, quizSize: Int)
        fun showQuizResult(isCorrect: Boolean, score: Int)
        fun showMessage(message: String)
        fun finishQuiz(quizScore:String)
    }

    interface Presenter {
        fun loadQuizQuestion()
        fun checkAnswer(selectedOption: Int)
    }
}
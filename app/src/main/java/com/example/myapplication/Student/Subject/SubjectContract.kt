package com.example.myapplication.Student.Subject

import com.example.myapplication.Models.Subject

interface SubjectContract {
    interface View {
        fun showSubjects(subjects: List<Subject>)
        fun showErrorMessage(message: String)
    }

    interface Presenter {
        fun loadSubjects()
    }
}
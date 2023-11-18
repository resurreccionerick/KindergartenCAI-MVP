package com.example.myapplication.Teacher.Teacher_Dashboard

interface TeacherDashboardContract {

    interface View {
        fun logoutSuccess()
        fun showMessage(message: String)
        fun goToProfile()
    }

    interface Presenter {
        fun logout()
    }

}
package com.example.myapplication.Student.Student_Dashboard

interface StudentDashboardContract {

    interface View {
        fun logoutSuccess()
        fun showMessage(message:String)
        fun goToProfile()
    }

    interface Presenter {
        fun logout()
    }

}
package com.example.myapplication.Teacher.Teacher_Dashboard

import com.google.firebase.auth.FirebaseAuth

class TeacherDashboardPresenter(private val view: TeacherDashboardActivity) :
    TeacherDashboardContract.Presenter {

    override fun logout() {
        FirebaseAuth.getInstance().currentUser?.let { currentUser ->
            FirebaseAuth.getInstance().signOut()
            view.logoutSuccess()
            view.showMessage("Successfully signed out")
        } ?: run {
            view.showMessage("No user logged in")
        }
    }
}

package com.example.myapplication.Student.Student_Dashboard

import com.google.firebase.auth.FirebaseAuth

class StudentDashboardPresenter(private val view: StudentDashboardActivity): StudentDashboardContract.Presenter {
    private val firebaseAuth = FirebaseAuth.getInstance()

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
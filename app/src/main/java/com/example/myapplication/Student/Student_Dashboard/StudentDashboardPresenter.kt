package com.example.myapplication.Student.Student_Dashboard

import com.google.firebase.auth.FirebaseAuth

class StudentDashboardPresenter(private val view: StudentDashboardActivity) : StudentDashboardContract.Presenter {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun logout() {
        firebaseAuth.currentUser?.let { currentUser ->
            firebaseAuth.signOut()
            view.logoutSuccess()
            view.showMessage("Successfully signed out")
        } ?: run {
            view.showMessage("No user is currently logged in")
        }
    }
}

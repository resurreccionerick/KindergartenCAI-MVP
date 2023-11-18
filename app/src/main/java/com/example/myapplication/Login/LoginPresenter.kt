package com.example.myapplication.Login

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.firestore.FirebaseFirestore

/**
This is the presenter that implements LoginContract.Presenter.
It interacts with the LoginContract.View.
In the doLogin method, it checks for email and password and triggers success or failure events accordingly. **/
class LoginPresenter(private val view: LoginContract.View) : LoginContract.Presenter {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    override fun doLogin(email: String, password: String) {
        val currentUser = firebaseAuth.currentUser
        if (email != null || password != null) {

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                if (currentUser != null) {
                    val userDocument =
                        firebaseFirestore.collection("Users").document(currentUser.uid)

                    userDocument.get()
                        .addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists()) {
                                val isStudent = documentSnapshot.getBoolean("isStudent")

                                if (isStudent != null) {
                                    if (isStudent) {
                                        // The "student" field is true
                                        // You can handle this case here
                                        view.onSuccess("Student login successful!", true)
                                    } else {
                                        // The "student" field is false
                                        // You can handle this case here
                                        view.onSuccess("Non-student login successful!", false)
                                    }
                                } else {
                                    // The "student" field is not present or is null
                                    view.onFailure("User document is missing the 'student' field")
                                }
                            } else {
                                // User document doesn't exist
                                view.onFailure("User document not found")
                            }
                        }
                        .addOnFailureListener { exception ->
                            // Handle failure
                            view.onFailure("Error while getting user data: ${exception.message}")
                        }
                } else {
                    // User is not logged in
                    view.onFailure("User not logged in")
                }
            }.addOnFailureListener {
                Log.e("login error: ", it.message.toString())
                view.onFailure("Error: " + it.message.toString())
            }
        } else {
            view.onFailure("Please enter all fields")
        }
    }


//    override fun doLogin(email: String, password: String) {
//        if (email.isNotEmpty() || password.isNotEmpty()) {
//            auth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        view.onSuccess("Login successful!")
//                    } else {
//                        val errorMessage = when (task.exception) {
//                            is FirebaseAuthInvalidCredentialsException ->
//                                "Invalid email or password."
//
//                            else -> "Login failed. Please try again."
//                        }
//                        view.onFailure(errorMessage)
//                    }
//                }
//        } else {
//            view.onFailure("Please enter all fields")
//        }
//    }

    override fun autoLogin() {
        if (firebaseAuth.currentUser != null) {
            view.autoLogin("Welcome back! " + firebaseAuth.currentUser?.email.toString())
        }
    }


}
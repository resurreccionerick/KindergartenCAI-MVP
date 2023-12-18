package com.example.myapplication.Login.Register

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.myapplication.Login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class RegisterPresenter(private val view: RegisterContract.View) : RegisterContract.Presenter {
    private val auth = FirebaseAuth.getInstance()

    override fun doRegister(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        view.onSuccess("Successfully registered")
                    } else {
                        val errorMessage = when (val exception = task.exception) {
                            is FirebaseAuthWeakPasswordException ->
                                "Password is too weak, please choose a stronger password."

                            is FirebaseAuthInvalidCredentialsException ->
                                "Invalid email format, please enter a valid email address."

                            is FirebaseAuthException ->
                                "Registration failed: ${exception.message}"

                            else ->
                                "Registration failed. Please try again."
                        }
                        view.onFailure(errorMessage)
                    }
                }
        } else {
            view.onFailure("Please enter all fields")
        }
    }
}

package com.example.myapplication.Login.Forgot

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class ForgotPassPresenter(val view: ForgotPassActivity) : ForgotPassContract.Presenter {

    override fun forgotPass(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    view.message("Check your email")
                    view.forgotSendSuccess()
                } else {
                    val exception = task.exception
                    if (exception is FirebaseAuthUserCollisionException) {
                        // Handle the case where the email is not registered
                        view.message("Error: Email not registered")
                    } else {
                        // Handle other errors
                        view.message("Error: " + exception?.message)
                    }
                }
            }
    }
}

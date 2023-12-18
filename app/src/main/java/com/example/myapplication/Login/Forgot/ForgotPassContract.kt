package com.example.myapplication.Login.Forgot

interface ForgotPassContract {
    interface View {
        fun message(Message: String)
        fun forgotSendSuccess()
    }

    interface Presenter {
        fun forgotPass(email: String)
    }
}
package com.example.myapplication.Login.Register

interface RegisterContract {

    interface View {
        fun onSuccess(message: String);
        fun onFailure(message: String);
    }

    interface Presenter {
        fun doRegister(email: String, password: String);
    }

}
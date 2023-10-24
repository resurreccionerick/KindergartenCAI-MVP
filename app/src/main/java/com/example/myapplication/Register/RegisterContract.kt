package com.example.myapplication.Register

interface RegisterContract {

    interface View {
        fun onSuccess(message: String);
        fun onFailure(message: String);
    }

    interface Presenter {
        fun doRegister(email: String, password: String);
    }

}
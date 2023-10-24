package com.example.myapplication.Login

/**
This is the contract defining the interfaces for the view and the presenter.
It defines View with onSuccess and onFailure methods, and Presenter with a doLogin method. **/
interface LoginContract {
    interface View {
        fun onSuccess(message: String, isStudent: Boolean)
        fun onFailure(message: String)
        fun autoLogin(message: String)
    }

    interface Presenter {
        fun doLogin(email: String, password: String)
        fun autoLogin()
    }
}
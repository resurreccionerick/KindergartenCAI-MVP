package com.example.myapplication.Profile

import com.example.myapplication.Models.User

interface ProfileContract {

    interface View {
        fun showMessage(message: String)
        fun setData(user: MutableList<User>)
    }

    interface Presenter {
        fun loadProfile(ref: String)
    }

}
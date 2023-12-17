package com.example.myapplication.Student.Leaderboards

import com.example.myapplication.Models.User

interface LeaderboardContract {

    interface View {
        fun showMessage(message: String)
        fun setData(users: List<User>)
    }

    interface Presenter {
        fun loadScore()
    }
}
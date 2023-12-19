package com.example.myapplication.Admin.Student

import com.example.myapplication.Models.User

interface ManageStudentContract {
    interface View {
        fun showMessage(message: String)
        fun setData(user: MutableList<User>)
    }

    interface Presenter {
        fun loadStudent()
        fun setCheck(student: User)
    }
}
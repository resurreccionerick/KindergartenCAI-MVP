package com.example.myapplication.Admin.Teacher

import com.example.myapplication.Models.User

interface ManageTeacherContract {
    interface View {
        fun showMessage(message: String)
        fun setData(user: MutableList<User>)
    }

    interface Presenter {
        fun loadTeacher()
        fun setCheck(student: User)
    }
}
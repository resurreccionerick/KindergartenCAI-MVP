package com.example.myapplication.Admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.Admin.Student.ManageStudentActivity
import com.example.myapplication.Admin.Teacher.ManageTeacherActivity
import com.example.myapplication.databinding.ActivityAdminDashboardBinding

class AdminDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnStudent.setOnClickListener {
            startActivity(Intent(this@AdminDashboardActivity, ManageStudentActivity::class.java))
        }

        binding.btnTeacher.setOnClickListener {
            startActivity(Intent(this@AdminDashboardActivity, ManageTeacherActivity::class.java))
        }
    }

    override fun onBackPressed() {
        onBackPressed()
        finish()
    }
}
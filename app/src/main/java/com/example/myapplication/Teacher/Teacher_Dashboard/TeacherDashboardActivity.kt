package com.example.myapplication.Teacher.Teacher_Dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.Teacher.Subject.SubjectActivity
import com.example.myapplication.databinding.ActivityTeacherDashboardBinding

class TeacherDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeacherDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddSubj.setOnClickListener {
            val intent = Intent(this@TeacherDashboardActivity, SubjectActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}
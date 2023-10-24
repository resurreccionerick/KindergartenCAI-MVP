package com.example.myapplication.Student.Student_Dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityStudentdashboardBinding

class StudentDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentdashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentdashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}
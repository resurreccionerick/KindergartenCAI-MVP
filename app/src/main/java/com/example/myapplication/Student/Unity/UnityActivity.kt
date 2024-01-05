package com.example.myapplication.Student.Unity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.Student.Student_Dashboard.StudentDashboardActivity
import com.example.myapplication.databinding.ActivityUnityBinding

class UnityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUnityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUnityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }


    override fun onBackPressed() {
        startActivity(Intent(this, StudentDashboardActivity::class.java))
        finish()
    }
}
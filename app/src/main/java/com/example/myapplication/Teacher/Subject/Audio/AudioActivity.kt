package com.example.myapplication.Teacher.Subject.Audio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.Teacher.Teacher_Dashboard.TeacherDashboardActivity
import com.example.myapplication.databinding.ActivityAudioBinding

class AudioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAudioBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.fabAddAudio.setOnClickListener {
            startActivity(Intent(this@AudioActivity, AddAudioActivity::class.java))
        }
    }
}
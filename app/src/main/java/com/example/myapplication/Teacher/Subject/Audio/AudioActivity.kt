package com.example.myapplication.Teacher.Subject.Audio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.Teacher.Subject.Video.AddVideoActivity
import com.example.myapplication.Teacher.Teacher_Dashboard.TeacherDashboardActivity
import com.example.myapplication.databinding.ActivityAudioBinding

class AudioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAudioBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val subjID = intent.getStringExtra("subjectID")

        binding.fabAddAudio.setOnClickListener {
            val videoIntent = Intent(binding.root.context, AddAudioActivity::class.java)
            videoIntent.putExtra("subjectID", subjID.toString())
            Log.d("ID", "VideoActivity + " + subjID.toString())
            binding.root.context.startActivity(videoIntent)
        }
    }
}
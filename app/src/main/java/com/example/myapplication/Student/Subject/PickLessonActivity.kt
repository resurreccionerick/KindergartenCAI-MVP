package com.example.myapplication.Student.Subject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.Student.Subject.Audio.AudioActivity
import com.example.myapplication.Student.Subject.Quiz.QuizActivity
import com.example.myapplication.Student.Subject.Video.VideoActivity
import com.example.myapplication.databinding.ActivityPickLessonBinding

class PickLessonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPickLessonBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPickLessonBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val subjID = intent.getStringExtra("subjectID")

        binding.btnPickVideo.setOnClickListener {
            val intent = Intent(binding.root.context, VideoActivity::class.java)
            intent.putExtra("subjectID", subjID)
            binding.root.context.startActivity(intent)
        }

        binding.btnPickSound.setOnClickListener {
            val intent = Intent(binding.root.context, AudioActivity::class.java)
            intent.putExtra("subjectID", subjID)
            binding.root.context.startActivity(intent)
        }

        binding.btnPickQuiz.setOnClickListener {
            val intent = Intent(binding.root.context, QuizActivity::class.java)
            intent.putExtra("subjectID", subjID)
            binding.root.context.startActivity(intent)
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, SubjectActivity::class.java))
        finish()
    }
}
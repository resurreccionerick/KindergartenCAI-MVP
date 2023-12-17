package com.example.myapplication.Teacher.Subject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.Teacher.Subject.Audio.AudioActivity
import com.example.myapplication.Teacher.Subject.Quiz.TeacherQuizActivity
import com.example.myapplication.Teacher.Subject.Video.AddVideoActivity
import com.example.myapplication.Teacher.Subject.Video.VideoActivity
import com.example.myapplication.databinding.ActivityAddLessonBinding

class AddLessonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddLessonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLessonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val subjID = intent.getStringExtra("subjectID") //get from adapter

        binding.btnAddVideo.setOnClickListener {
            val intent = Intent(binding.root.context, VideoActivity::class.java)
            intent.putExtra("subjectID", subjID.toString())
            Log.d("ID", "AddLessonAct + " + subjID.toString())
            binding.root.context.startActivity(intent)
        }

        binding.btnAddQuiz.setOnClickListener {
            val intent = Intent(binding.root.context, TeacherQuizActivity::class.java)
            intent.putExtra("subjectID", subjID.toString())
            Log.d("ID", "AddLessonAct + " + subjID.toString())
            binding.root.context.startActivity(intent)
        }

        binding.btnAddSound.setOnClickListener {
            val intent = Intent(binding.root.context, AudioActivity::class.java)
            intent.putExtra("subjectID", subjID.toString())
            Log.d("ID", "AddLessonAct + " + subjID.toString())
            binding.root.context.startActivity(intent)
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, SubjectActivity::class.java))
        finish()
    }
}
package com.example.myapplication.Teacher.Subject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.Teacher.Subject.Video.AddVideoActivity
import com.example.myapplication.Teacher.Subject.Video.VideoActivity
import com.example.myapplication.databinding.ActivityAddLessonBinding

class AddLessonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddLessonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLessonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddVideo.setOnClickListener {
            val subjID = intent.getStringExtra("subjectID") //get from adapter

            val videoIntent = Intent(binding.root.context, VideoActivity::class.java)
            videoIntent.putExtra("subjectID", subjID.toString())
            Log.d("ID", "AddLessonAct + " + subjID.toString())
            binding.root.context.startActivity(videoIntent)
        }
    }
}
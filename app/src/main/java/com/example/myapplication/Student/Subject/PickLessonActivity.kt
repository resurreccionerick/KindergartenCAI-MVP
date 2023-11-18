package com.example.myapplication.Student.Subject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityPickLessonBinding

class PickLessonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPickLessonBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPickLessonBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }
}
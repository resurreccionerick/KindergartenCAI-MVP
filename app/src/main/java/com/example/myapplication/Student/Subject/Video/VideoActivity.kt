package com.example.myapplication.Student.Subject.Video

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Models.Video
import com.example.myapplication.databinding.ActivityVideo2Binding

class VideoActivity : AppCompatActivity(), VideoContract.View {
    private lateinit var binding: ActivityVideo2Binding
    private lateinit var presenter: VideoPresenter
    private lateinit var adapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideo2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val subjID = intent.getStringExtra("subjectID")

        presenter = VideoPresenter(this)

        adapter = VideoAdapter(emptyList())

        binding.rvVideo.layoutManager = LinearLayoutManager(this)
        binding.rvVideo.adapter = adapter

        presenter.loadVideo(subjID.toString())
    }

    override fun showVideo(subjects: List<Video>) {
        adapter.updateData(subjects)
    }

    override fun showErrorMessage(message: String) {
        showToast(message)
    }

    override fun showSuccessMessage(message: String) {
        showToast(message)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
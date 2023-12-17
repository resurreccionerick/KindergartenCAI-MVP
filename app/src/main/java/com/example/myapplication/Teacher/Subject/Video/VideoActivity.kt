package com.example.myapplication.Teacher.Subject.Video

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Models.Video
import com.example.myapplication.Teacher.Subject.AddLessonActivity
import com.example.myapplication.Teacher.Subject.SubjectActivity
import com.example.myapplication.databinding.ActivityVideoBinding

class VideoActivity : AppCompatActivity(), VideoContract.View {
    private lateinit var binding: ActivityVideoBinding
    private lateinit var presenter: VideoPresenter
    private lateinit var adapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = VideoPresenter(this)

        val subjID = intent.getStringExtra("subjectID")

        adapter = VideoAdapter(
            emptyList(),
            onDeleteClick = { video ->
                showErrorMessage("Video deleted: ${video.title}")
                presenter.deleteVideo(subjID.toString(), video.id.toString(), video)
            },
            onEditClick = { video ->
                val intent = Intent(this, EditVideoActivity::class.java)
                intent.putExtra("videoId", video.id)
                intent.putExtra("videoTitle", video.title)
                intent.putExtra("subjectVideo", video.videoUrl)
                intent.putExtra("subjectID", subjID)
                startActivity(intent)
            }
        )
        binding.rvAddVideoList.layoutManager = LinearLayoutManager(this)
        binding.rvAddVideoList.adapter = adapter

        binding.fabAddVideoList.setOnClickListener {
            val videoIntent = Intent(binding.root.context, AddVideoActivity::class.java)
            videoIntent.putExtra("subjectID", subjID.toString())
            Log.d("ID", "VideoActivity + " + subjID.toString())
            binding.root.context.startActivity(videoIntent)
        }

        presenter.loadVideo(subjID.toString())

    }

    override fun showVideo(subjects: List<Video>) {
        // Update the adapter with the list of video
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

    override fun onBackPressed() {
        startActivity(Intent(this, SubjectActivity::class.java))
        finish()
    }
}
package com.example.myapplication.Teacher.Subject.Audio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models.Audio
import com.example.myapplication.R
import com.example.myapplication.Teacher.Subject.Video.AddVideoActivity
import com.example.myapplication.Teacher.Subject.Video.EditVideoActivity
import com.example.myapplication.Teacher.Teacher_Dashboard.TeacherDashboardActivity
import com.example.myapplication.databinding.ActivityAudioBinding

class AudioActivity : AppCompatActivity(), AddAudioContract.AudioView {
    private lateinit var binding: ActivityAudioBinding
    private lateinit var adapter: AudioAdapter
    private lateinit var presenter: AudioPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAudioBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val subjID = intent.getStringExtra("subjectID")

        presenter = AudioPresenter(this)

        // Initialize the RecyclerView and adapter
        val recyclerView: RecyclerView = binding.rvAudio

        adapter = AudioAdapter(emptyList(),
            onDeleteClick = { audio ->
                showMessage("Audio deleted: ${audio.title}")
                presenter.deleteAudio(subjID.toString(), audio.id.toString(), audio)
            },
            onEditClick = { audio ->
                val intent = Intent(this, EditAudioActivity::class.java)
                intent.putExtra("audioId", audio.id)
                intent.putExtra("audioTitle", audio.title)
                intent.putExtra("audioUrl", audio.audioUrl)
                intent.putExtra("audioAudioPic", audio.audioPic)
                intent.putExtra("subjectID", subjID)
                startActivity(intent)
            })

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        binding.fabAddAudio.setOnClickListener {
            val videoIntent = Intent(binding.root.context, AddAudioActivity::class.java)
            videoIntent.putExtra("subjectID", subjID.toString())
            Log.d("ID", "VideoActivity + " + subjID.toString())
            binding.root.context.startActivity(videoIntent)
        }

        presenter.loadAudio(subjID.toString())
    }

    override fun showAudio(audio: List<Audio>) {
        adapter.updateData(audio)
    }

    override fun showMessage(message: String) {
        showToast(message)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}
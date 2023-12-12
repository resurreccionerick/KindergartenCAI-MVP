package com.example.myapplication.Student.Subject.Audio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models.Audio
import com.example.myapplication.Models.Video
import com.example.myapplication.R
import com.example.myapplication.Student.Subject.SubjectAdapter
import com.example.myapplication.databinding.ActivityAudio2Binding

class AudioActivity : AppCompatActivity(), AudioContract.View {
    private lateinit var binding: ActivityAudio2Binding
    private lateinit var presenter: AudioPresenter
    private lateinit var adapter: AudioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudio2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val subjID = intent.getStringExtra("subjectID")

        presenter = AudioPresenter(this)

        // Initialize the RecyclerView and adapter
        val recyclerView: RecyclerView = binding.rvAudio

        adapter = AudioAdapter(emptyList())

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

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
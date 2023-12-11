package com.example.myapplication.Student.Subject.Audio

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models.Audio
import com.example.myapplication.databinding.AudioItemBinding
import com.squareup.picasso.Picasso

class AudioAdapter(private var audio: List<Audio>) :
    RecyclerView.Adapter<AudioAdapter.AudioViewHolder>() {

    private var mediaPlayer: MediaPlayer? = null

    inner class AudioViewHolder(val binding: AudioItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(audio: Audio) {
            binding.fabVideoEdit.isVisible = false
            binding.fabVideoDelete.isVisible = false

            binding.txtTitle.text = audio.title

            binding.fabAudioPlay.setOnClickListener {
                playAudio(audio.audioUrl.toString())
            }

            Picasso.get().load(audio.audioPic).into(binding.imgAudio)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val binding = AudioItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AudioViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return audio.size
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        val currentAudio = audio[position]
        holder.bind(currentAudio)
    }

    fun updateData(audioList: List<Audio>) {
        audio = audioList
        notifyDataSetChanged()
    }

    private fun playAudio(audioUrl: String) {
        mediaPlayer?.release() // Release any existing MediaPlayer instance

        mediaPlayer = MediaPlayer().apply {
            setDataSource(audioUrl)
            prepare()
            start()
        }

        // Optionally, you can add an OnCompletionListener to release the MediaPlayer when the playback is complete
        mediaPlayer?.setOnCompletionListener {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }
}

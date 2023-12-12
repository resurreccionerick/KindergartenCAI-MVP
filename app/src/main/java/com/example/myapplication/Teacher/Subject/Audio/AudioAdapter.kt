package com.example.myapplication.Teacher.Subject.Audio

import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models.Audio
import com.example.myapplication.R
import com.example.myapplication.databinding.AudioItemBinding
import com.squareup.picasso.Picasso


class AudioAdapter(
    private var audio: List<Audio>,
    private val onEditClick: (Audio) -> Unit,
    private val onDeleteClick: (Audio) -> Unit
) :
    RecyclerView.Adapter<AudioAdapter.AudioViewHolder>() {

    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false

    inner class AudioViewHolder(val binding: AudioItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            // Initialize isPlaying to the default state (e.g., false)
            isPlaying = false
        }

        fun bind(audio: Audio) {

            binding.fabVideoEdit.setOnClickListener {
                onEditClick(audio)
            }

            binding.fabVideoDelete.setOnClickListener {
                onDeleteClick(audio)
            }

            binding.txtTitle.text = audio.title

            binding.fabAudioPlay.setOnClickListener {
                if (isPlaying) {
                    // Change to the paused state
                    binding.fabAudioPlay.setImageResource(R.drawable.baseline_play_arrow_24)
                    pauseAudio()
                } else {
                    // Change to the playing state
                    binding.fabAudioPlay.setImageResource(R.drawable.baseline_pause_24)
                    playAudio(audio.audioUrl.toString())
                }
                // Toggle the state
                isPlaying = !isPlaying

                // Log the current state
                Log.d("AudioAdapter", "isPlaying: $isPlaying")
            }

            Picasso.get().load(audio.audioPic).into(binding.imgAudio)
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AudioAdapter.AudioViewHolder {
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

    fun playAudio(audioUrl: String) {
        mediaPlayer?.release() // Release any existing MediaPlayer instance

        mediaPlayer = MediaPlayer().apply {
            setDataSource(audioUrl)
            prepare()
            start()
        }

        mediaPlayer?.setOnCompletionListener {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    private fun pauseAudio() {
        mediaPlayer?.pause()
    }
}

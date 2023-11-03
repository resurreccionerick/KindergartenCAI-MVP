package com.example.myapplication.Teacher.Subject.Video

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models.Video
import com.example.myapplication.databinding.VideoItemBinding


class VideoAdapter(
    private var videos: List<Video>,
    private val onEditClick: (Video) -> Unit,
    private val onDeleteClick: (Video) -> Unit
) :
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(private val binding: VideoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isPlaying = false // Track video playing state

        fun bind(video: Video) {

            // Convert the videoUrl string to a URI
            val videoUri = Uri.parse(video.videoUrl)

            // Set the video URI and start playing
            binding.videoViewVideo.setVideoURI(videoUri)

            // Set an OnClickListener to toggle video playback
            binding.videoViewVideo.setOnClickListener {
                if (isPlaying) {
                    binding.videoViewVideo.pause()
                } else {
                    binding.videoViewVideo.start()
                }
                isPlaying = !isPlaying
            }

            binding.txtTitle.text = video.title

            binding.fabVideoDelete.setOnClickListener {
                onDeleteClick(video)
            }

            binding.fabVideoEdit.setOnClickListener {
                onEditClick(video)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = VideoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VideoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videos[position])
    }

    fun updateData(videoList: List<Video>) {
        videos = videoList
        notifyDataSetChanged()
    }
}

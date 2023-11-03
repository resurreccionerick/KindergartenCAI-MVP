package com.example.myapplication.Teacher.Subject.Video

import com.example.myapplication.Models.Video

interface VideoContract {
    interface View {
        fun showVideo(subjects: List<Video>)
        fun showErrorMessage(message: String)
        fun showSuccessMessage(message: String)
    }

    interface Presenter {
        fun loadVideo(id: String)
        fun deleteVideo(subjId: String, videoId: String, video: Video)
    }
}
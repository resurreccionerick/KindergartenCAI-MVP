package com.example.myapplication.Student.Subject.Audio

import com.example.myapplication.Models.Audio

interface AudioContract {

    interface View {
        fun showMessage(message: String)
        fun showAudio(audio: List<Audio>)
    }

    interface Presenter {
        fun loadAudio(id: String)
    }
}
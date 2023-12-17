package com.example.myapplication.Student.Subject.Audio

import android.util.Log
import com.example.myapplication.Models.Audio
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AudioPresenter(val view: AudioActivity) : AudioContract.Presenter {
    override fun loadAudio(id: String) {
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("Subjects").child(id).child("Audio")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val audio = mutableListOf<Audio>()
                for (childSnapshot in dataSnapshot.children) {
                    val id = childSnapshot.child("id").value.toString()
                    val title = childSnapshot.child("title").value.toString()
                    val audioUrl = childSnapshot.child("audioUrl").value.toString()
                    val audioPic = childSnapshot.child("audioPic").value.toString()
                    val values = Audio(id, title, audioPic, audioUrl)

                    audio.add(values)
                }

                if (audio.isNotEmpty()) {
                    view.showAudio(audio)
                } else {
                    view.finish()
                    view.showMessage("No Audio Yet")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                view.showMessage("Error: " + databaseError.message)
            }
        })
    }
}
package com.example.myapplication.Teacher.Subject.Audio

import com.example.myapplication.Models.Audio
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AudioPresenter(var view: AudioActivity) : AddAudioContract.AudioPresenter {
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
                    view.showMessage("No Audio Yet")
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                view.showMessage("Error: " + databaseError.message)
            }
        })
    }

    override fun deleteAudio(subjID: String, audioID: String, audio: Audio) {
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("Subjects").child(subjID).child("Audio")
                .child(audioID)

        databaseReference.removeValue().addOnCompleteListener {
            view.showMessage(audio.title + " Audio Deleted")
        }.addOnFailureListener {
            view.showMessage(it.message.toString())
        }
    }
}
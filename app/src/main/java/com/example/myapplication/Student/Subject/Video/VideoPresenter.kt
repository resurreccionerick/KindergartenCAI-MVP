package com.example.myapplication.Student.Subject.Video

import com.example.myapplication.Models.Video
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VideoPresenter(var view: VideoActivity) : VideoContract.Presenter {
    override fun loadVideo(id: String) {
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("Subjects").child(id).child("Video")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val video = mutableListOf<Video>()
                for (childSnapshot in dataSnapshot.children) {
                    val id = childSnapshot.child("id").value.toString()
                    val title = childSnapshot.child("title").value.toString()
                    val videoUrl = childSnapshot.child("videoUrl").value.toString()
                    val subject = Video(id, title, videoUrl)

                    video.add(subject)
                }
                // Check if the video list is not null or empty before showing it
                if (video.isNotEmpty()) {
                    view.showVideo(video)
                } else {
                    // If video list is null or empty, show a toast
                    view.finish()
                    view.showErrorMessage("No Videos Yet")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                view.showErrorMessage("Error: " + databaseError.message)
            }
        })
    }
}
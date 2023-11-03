package com.example.myapplication.Teacher.Subject.Video

import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.myapplication.Models.Video
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class AddVideoPresenter(var view: AddVideoActivity) : AddVideoContract.Presenter {

    override fun uploadVideo(intent: Intent, subjectId: String?, title: String?, videoUri: Uri) {
        val video = Video(subjectId, title, "")
        Log.d("ID", "AddVideoPresenter" + subjectId.toString())

        val databaseReference =
            FirebaseDatabase.getInstance().getReference("Subjects").child(
                subjectId.toString()).child("Video")

        val storageReference = FirebaseStorage.getInstance().reference

        val videoReference = databaseReference.push()
        val newVideoId = videoReference.key

        if (videoUri != null) { // Check if videoUri is not null
            val videoRef: StorageReference =
                storageReference.child("subject_videos/${newVideoId}.mp4")

            val uploadTask: UploadTask = videoRef.putFile(videoUri)

            uploadTask.addOnSuccessListener { taskSnapshot ->
                // Video upload success
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    val videoUrl = uri.toString()
                    video.id = newVideoId
                    video.videoUrl = videoUrl // Update the video URL
                    videoReference.setValue(video)
                    view.onVideoUploaded()
                    view.showSuccessMessage("Video Successfully Uploaded.")
                }
            }.addOnFailureListener {
                // Video upload failure
                view.showErrorMessage("Video upload failed")
            }
        } else {
            view.showErrorMessage("Invalid video source. Please select a video from the gallery.")
        }
    }

    override fun onCameraPermissionGranted() {
        view.captureVideoFromCamera()
    }

    override fun onStoragePermissionGranted() {
        view.pickVideoFromGallery()
    }
}

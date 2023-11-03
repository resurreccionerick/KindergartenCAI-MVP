package com.example.myapplication.Teacher.Subject.Video

import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.myapplication.Models.Subject
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask


class EditVideoPresenter(private val view: EditVideoActivity, private val subjId: String) :
    EditVideoContract.Presenter {

    private val databaseReference =
        FirebaseDatabase.getInstance().reference.child("Subjects").child(subjId).child("Video")
    private val storageReference = FirebaseStorage.getInstance().reference

    override fun uploadEditVideo(
        intent: Intent,
        videoId: String?,
        title: String?,
        selectedVideo: Uri?
    ) {


        //val videoId = intent.getStringExtra("videoId")

        if (videoId != null) {
            // Query the database for the subject with the specified subjectId
            databaseReference.child(videoId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val currentTitle = dataSnapshot.child("title").value.toString()
                            Log.d("ERICK", dataSnapshot.child("title").value.toString())

                            // Check if the title and Video are being changed
                            val isTitleChanged = title != currentTitle
                            val isVideoChanged = selectedVideo != null

                            if (isTitleChanged || isVideoChanged) {
                                if (isVideoChanged) {
                                    // Upload the new Video to Firebase Storage
                                    val videoRef: StorageReference =
                                        storageReference.child("subject_videos/$videoId.mp4")
                                    val uploadTask: UploadTask = videoRef.putFile(selectedVideo!!)

                                    uploadTask.addOnSuccessListener { taskSnapshot ->
                                        // Get the download URL of the uploaded Video
                                        videoRef.downloadUrl.addOnSuccessListener { uri ->
                                            val newVideoUrl = uri.toString()
                                            if (isTitleChanged) {
                                                // Update both title and Video
                                                updateSubjectWithVideoAndTitle(
                                                    videoId,
                                                    title!!,
                                                    newVideoUrl
                                                )
                                            } else {
                                                // Update only the Video
                                                updateSubjectWithVideo(
                                                    videoId,
                                                    title!!,
                                                    newVideoUrl
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    // Update only the title
                                    updateSubjectWithTitle(videoId, title!!)
                                }
                            } else {
                                // Nothing to update
                                view.showSuccessMessage("No changes to update.")
                            }
                        } else {
                            view.showErrorMessage("Error: Video not found in database")
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        view.showErrorMessage("Error: " + databaseError.message)
                    }
                })
        } else {
            view.showErrorMessage("Error: Video not found!")
        }
    }

    private fun updateSubjectWithVideoAndTitle(subjectId: String, title: String, videoUrl: String) {
        val subject = Subject(subjectId, title, videoUrl)
        databaseReference.child(subjectId).child("Video").setValue(subject)
        view.onVideoUploaded()
        view.showSuccessMessage("Video title and Video successfully updated.")
    }


    private fun updateSubjectWithVideo(subjectId: String, title: String, videoUrl: String) {
        val video = Subject(subjectId, title, videoUrl)
        databaseReference.child(subjectId).child("Video").child("videoUrl").setValue(videoUrl)
        view.onVideoUploaded()
        view.showSuccessMessage("Subject Video successfully updated.")
    }

    private fun updateSubjectWithTitle(subjectId: String, title: String) {
        databaseReference.child(subjectId).child("Video").child("title").setValue(title)
        view.onVideoUploaded()
        view.showSuccessMessage("Subject title successfully updated.")
    }

    override fun onCameraPermissionGranted() {
        view.captureVideoFromCamera()
    }

    override fun onStoragePermissionGranted() {
        view.pickVideoFromGallery()
    }

}
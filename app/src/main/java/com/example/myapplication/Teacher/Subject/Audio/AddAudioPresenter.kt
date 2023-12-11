package com.example.myapplication.Teacher.Subject.Audio

import android.content.Intent
import android.net.Uri
import com.example.myapplication.Models.Audio
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class AddAudioPresenter(var view: AddAudioActivity) : AddAudioContract.Presenter {
    override fun uploadAudio(
        intent: Intent,
        subjectId: String,
        title: String,
        audioUri: Uri,
        imageUri: Uri
    ) {
        val audio = Audio(subjectId, title, "", "")

        val databaseReference =
            FirebaseDatabase.getInstance().getReference("Subjects").child(subjectId).child("Audio")

        val storageReference = FirebaseStorage.getInstance().reference

        val reference = databaseReference.push()
        val newAudioId = reference.key

        if (audioUri != null && imageUri != null) { // Check if AudioUri & imageUri is not null
            val audioRef: StorageReference =
                storageReference.child("subject_audio/${newAudioId}.mp3")

            val audioImgRef: StorageReference =
                storageReference.child("subject_audio_img/${newAudioId}.jpeg")

            val uploadTask: UploadTask = audioRef.putFile(audioUri)
            val uploadTaskImg: UploadTask = audioImgRef.putFile(imageUri)

            uploadTask.addOnSuccessListener { taskSnapshot ->
                // Audio upload success
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    val audioUrl = uri.toString()
                    audio.id = newAudioId
                    audio.audioUrl = audioUrl // Update the Audio URL
                    reference.setValue(audio)
                    view.showSuccessMessage("Audio Successfully Uploaded.")
                }
            }.addOnFailureListener {
                // Audio upload failure
                view.showErrorMessage("Audio upload failed")
            }

            uploadTaskImg.addOnSuccessListener { taskSnapshot ->
                // Audio upload success
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    val audioImgUrl = uri.toString()
                    audio.audioPic = audioImgUrl // Update the Audio img
                    reference.setValue(audio)
                    view.onAudioUploaded()
                }
            }.addOnFailureListener {
                // Audio upload failure
                view.showErrorMessage("Image upload failed")
            }
        }
    }


    override fun onCameraPermissionGranted() {
        view.captureImageFromCamera()
    }

    override fun onStoragePermissionGranted() {
        view.pickImageFromGallery()
    }

    override fun onAudioStoragePermissionGranted() {
        view.pickAudio()
    }
}
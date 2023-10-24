package com.example.myapplication.Teacher.Subject

import android.net.Uri
import android.util.Log
import com.example.myapplication.Models.Subject
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class AddSubjectPresenter(private val view: AddSubjectContract.View) :
    AddSubjectContract.Presenter {

    // Reference to the Firebase Realtime Database
    private val databaseReference = FirebaseDatabase.getInstance().getReference("Subjects")

    // Reference to the Firebase Storage
    private val storageReference = FirebaseStorage.getInstance().reference


    override fun uploadSubject(title: String, selectedImageUri: Uri) {

        // Generate a unique key for the subject
        val subjectId = databaseReference.push().key

        // Create a Subject object with title
        val subject = Subject(title, "")

        // Push the subject to the database
        if (subjectId != null) {
            // Upload the image to Firebase Storage
            val imageRef: StorageReference =
                storageReference.child("subject_images/$subjectId.jpg")
            val uploadTask: UploadTask = imageRef.putFile(selectedImageUri)

            uploadTask.addOnSuccessListener { taskSnapshot ->
                // Image upload success
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    subject.imageUrl = imageUrl
                    databaseReference.child(subjectId).setValue(subject)
                    view.onSubjectUploaded()
                    view.showSuccessMessage("Subject Successfully Uploaded.")
                }
            }.addOnFailureListener { exception ->
                // Image upload failure
                Log.e("UploadFailure", exception.message ?: "Unknown error")
                view.showErrorMessage("Subject upload failed")
            }
        } else {
            view.showErrorMessage("Subject upload failed")
        }


    }

    override fun onUploadButtonClick() {
        view.requestCameraPermission()
    }

    override fun onCameraPermissionGranted() {
        view.pickImageFromGallery()
    }

    override fun onStoragePermissionGranted() {
        view.pickImageFromGallery()
    }
}

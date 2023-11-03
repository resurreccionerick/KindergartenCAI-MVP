package com.example.myapplication.Teacher.Subject

import android.net.Uri
import com.example.myapplication.Models.Subject
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import android.content.Intent
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class AddSubjectPresenter(private val view: AddSubjectContract.View) :
    AddSubjectContract.Presenter {

    private val databaseReference = FirebaseDatabase.getInstance().getReference("Subjects")
    private val storageReference = FirebaseStorage.getInstance().reference

    override fun uploadSubject(intent: Intent, title: String, selectedImageUri: Uri) {
        val subjectId = intent.getStringExtra("subjectId")
        val subject = Subject(subjectId, title, "")

        val subjectReference = databaseReference.push()
        val newSubjectId = subjectReference.key

        if (selectedImageUri.scheme == "content") {
            // Handle gallery image upload for adding a new subject
            val imageRef: StorageReference =
                storageReference.child("subject_images/$newSubjectId.jpg")
            val uploadTask: UploadTask = imageRef.putFile(selectedImageUri)

            uploadTask.addOnSuccessListener { taskSnapshot ->
                // Image upload success
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    subject.id = newSubjectId
                    subject.imageUrl = imageUrl
                    subjectReference.setValue(subject)
                    view.onSubjectUploaded()
                    view.showSuccessMessage("Subject Successfully Uploaded.")
                }
            }.addOnFailureListener {
                // Image upload failure
                view.showErrorMessage("Subject upload failed")
            }
        } else {
            // Handle other types of image URIs (e.g., web URLs or file URIs)
            view.showErrorMessage("Invalid image source. Please select an image from the gallery.")
        }

    }

    override fun onCameraPermissionGranted() {
        view.captureImageFromCamera()
    }

    override fun onStoragePermissionGranted() {
        view.pickImageFromGallery()
    }

}

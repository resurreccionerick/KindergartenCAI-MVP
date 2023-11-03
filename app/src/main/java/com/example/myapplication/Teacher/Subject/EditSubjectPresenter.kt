package com.example.myapplication.Teacher.Subject

import android.content.Intent
import android.net.Uri
import com.example.myapplication.Models.Subject
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class EditSubjectPresenter(private val view: EditSubjectActivity) : EditSubjectContract.Presenter {
    private val databaseReference = FirebaseDatabase.getInstance().reference.child("Subjects")
    private val storageReference = FirebaseStorage.getInstance().reference

    private fun updateSubjectWithImageAndTitle(subjectId: String, title: String, imageUrl: String) {
        val subject = Subject(subjectId, title, imageUrl)
        databaseReference.child(subjectId).setValue(subject)
        view.onSubjectUploaded()
        view.showSuccessMessage("Subject title and image successfully updated.")
    }


    private fun updateSubjectWithImage(subjectId: String, title: String, imageUrl: String) {
        val subject = Subject(subjectId, title, imageUrl)
        databaseReference.child(subjectId).child("imageUrl").setValue(imageUrl)
        view.onSubjectUploaded()
        view.showSuccessMessage("Subject image successfully updated.")
    }

    private fun updateSubjectWithTitle(subjectId: String, title: String) {
        databaseReference.child(subjectId).child("title").setValue(title)
        view.onSubjectUploaded()
        view.showSuccessMessage("Subject title successfully updated.")
    }


    override fun uploadEditSubject(
        intent: Intent,
        id: String?,
        title: String?,
        selectedImageUri: Uri?
    ) {
        val subjectId = intent.getStringExtra("subjectId")

        if (subjectId != null) {
            // Query the database for the subject with the specified subjectId
            databaseReference.child(subjectId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val currentTitle = dataSnapshot.child("title").value.toString()

                            // Check if the title and image are being changed
                            val isTitleChanged = title != currentTitle
                            val isImageChanged = selectedImageUri != null

                            if (isTitleChanged || isImageChanged) {
                                if (isImageChanged) {
                                    // Upload the new image to Firebase Storage
                                    val imageRef: StorageReference =
                                        storageReference.child("images/$subjectId.jpg")
                                    val uploadTask: UploadTask = imageRef.putFile(selectedImageUri!!)

                                    uploadTask.addOnSuccessListener { taskSnapshot ->
                                        // Get the download URL of the uploaded image
                                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                                            val newImageUrl = uri.toString()
                                            if (isTitleChanged) {
                                                // Update both title and image
                                                updateSubjectWithImageAndTitle(
                                                    subjectId,
                                                    title!!,
                                                    newImageUrl
                                                )
                                            } else {
                                                // Update only the image
                                                updateSubjectWithImage(
                                                    subjectId,
                                                    title!!,
                                                    newImageUrl
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    // Update only the title
                                    updateSubjectWithTitle(subjectId, title!!)
                                }
                            } else {
                                // Nothing to update
                                view.showSuccessMessage("No changes to update.")
                            }
                        } else {
                            view.showErrorMessage("Error: Subject not found")
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        view.showErrorMessage("Error: " + databaseError.message)
                    }
                })
        } else {
            view.showErrorMessage("Error: Subject not found")
        }
    }


    override fun onCameraPermissionGranted() {
        view.captureImageFromCamera()
    }

    override fun onStoragePermissionGranted() {
        view.pickImageFromGallery()
    }
}

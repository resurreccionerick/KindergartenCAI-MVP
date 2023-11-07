package com.example.myapplication.Teacher.Subject.Quiz

import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.myapplication.Models.Quiz
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class EditQuizPresenter(subjectId: String, quizId: String, var view: EditQuizActivity) :
    EditQuizContract.Presenter {

    private val databaseReference =
        FirebaseDatabase.getInstance().getReference("Subjects").child(
            subjectId
        ).child("Quiz").child(quizId)
    private val storageReference = FirebaseStorage.getInstance().reference

    override fun uploadEditQuiz(
        intent: Intent,
        quizId: String?,
        title: String?,
        opt1: String?,
        opt2: String?,
        opt3: String?,
        opt4: String?,
        titleImage: Uri?
    ) {
        if (title != null && opt1 != null && opt2 != null && opt3 != null && opt4 != null && titleImage != null) {
            databaseReference
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val currentTitle = dataSnapshot.child("title").value.toString()
                            // Check if the title and image are being changed
                            val isTitleChanged = title != currentTitle
                            val isImageChanged = titleImage != null
                            Log.d("Erick", title + " " + currentTitle)

                            if (isTitleChanged) {
                                //if (isImageChanged) {
                                // Upload the new image to Firebase Storage
                                val imageRef: StorageReference =
                                    storageReference.child("quiz_images/$quizId.jpg")
                                val uploadTask: UploadTask = imageRef.putFile(titleImage!!)

                                uploadTask.addOnSuccessListener { taskSnapshot ->
                                    // Get the download URL of the uploaded image
                                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                                        val newImageUrl = uri.toString()
                                        if (isTitleChanged) {
                                            val quiz = Quiz(
                                                quizId,
                                                title.toString(),
                                                opt1.toString(),
                                                opt2.toString(),
                                                opt3.toString(),
                                                opt4.toString(),
                                                newImageUrl
                                            )
                                            databaseReference.setValue(quiz)
                                            view.onQuizUploaded()
                                            view.showSuccessMessage("Subject title and image successfully updated.")
                                        }
                                    }
                                }
                                //}
                            }
                        } else {
                            view.showErrorMessage("Error: Subject not found")
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        view.showErrorMessage("Error: " + databaseError.message)
                    }
                })
        }else{
            view.showErrorMessage("Please enter all fields")
        }
    }


    override fun onCameraPermissionGranted() {
        view.captureImageFromCamera()
    }

    override fun onStoragePermissionGranted() {
        view.pickImageFromGallery()
    }
}
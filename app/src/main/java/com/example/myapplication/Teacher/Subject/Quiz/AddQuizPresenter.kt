package com.example.myapplication.Teacher.Subject.Quiz

import android.content.Intent
import android.net.Uri
import com.example.myapplication.Models.Quiz
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class AddQuizPresenter(intent: Intent, subjectId: String, var view: AddQuizActivity) :
    AddQuizContract.Presenter {

    private val databaseReference =
        FirebaseDatabase.getInstance().getReference("Subjects").child(
            subjectId
        ).child("Quiz")

    private val storageReference = FirebaseStorage.getInstance().reference

    private val quizReference = databaseReference.push()
    private val newVideoId = quizReference.key

    override fun uploadQuiz(
        intent: Intent,
        title: String,
        opt1: String,
        opt2: String,
        opt3: String,
        opt4: String,
        selectedImageTitleUri: Uri
    ) {
        val quiz = Quiz(newVideoId, title, opt1, opt2, opt3, opt4, "")

        if (selectedImageTitleUri.scheme == "content") {
            // Handle gallery image upload for adding a new subject
            val imageRef: StorageReference =
                storageReference.child("quiz_images/$newVideoId.jpg")
            val uploadTask: UploadTask = imageRef.putFile(selectedImageTitleUri)

            uploadTask.addOnSuccessListener { taskSnapshot ->
                // Image upload success
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    quiz.id = newVideoId
                    quiz.title = title
                    quiz.opt1 = opt1
                    quiz.opt2 = opt2
                    quiz.opt4 = opt3
                    quiz.opt4 = opt4
                    quiz.titleImgUrl = imageUrl
                    quizReference.setValue(quiz)
                    view.onQuizUploaded()
                    view.showSuccessMessage("Quiz Successfully Uploaded.")
                }
            }.addOnFailureListener {
                // Image upload failure
                view.showErrorMessage("Quiz upload failed")
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

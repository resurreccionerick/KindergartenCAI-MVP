package com.example.myapplication.Admin.Student.Register

import android.content.Intent
import android.net.Uri
import com.example.myapplication.Models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class StudentRegisterPresenter(private val view: StudentRegisterActivity) :
    StudentRegisterContract.Presenter {
    private val auth = FirebaseAuth.getInstance()
    private val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
    private val storageReference = FirebaseStorage.getInstance().reference

    override fun doRegister(
        name: String,
        email: String,
        password: String,
        intent: Intent,
        selectedImageUri: Uri?
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userReference = databaseReference.push()
                    val newStudentId = userReference.key

                    if (selectedImageUri!!.scheme == "content") {
                        // Handle gallery image upload for adding a new user
                        val imageRef: StorageReference =
                            storageReference.child("student_images/$newStudentId.jpg")
                        val uploadTask: UploadTask = imageRef.putFile(selectedImageUri!!)

                        uploadTask.addOnSuccessListener { taskSnapshot ->
                            // Image upload success
                            taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                                val imageUrl = uri.toString()

                                val user = User(
                                    newStudentId,
                                    name,
                                    email,
                                    "0",
                                    "true",
                                    imageUrl
                                )
                                userReference.setValue(user)
                                view.onStudentUploaded()
                                view.onSuccess("User Successfully Registered.")
                            }
                        }.addOnFailureListener {
                            // Image upload failure
                            view.onFailure("User upload failed")
                        }
                    } else {
                        // Handle other types of image URIs (e.g., web URLs or file URIs)
                        view.onFailure("Invalid image source. Please select an image from the gallery.")
                    }
                } else {
                    val errorMessage = when (val exception = task.exception) {
                        is FirebaseAuthWeakPasswordException ->
                            "Password is too weak, please choose a stronger password."

                        is FirebaseAuthInvalidCredentialsException ->
                            "Invalid email format, please enter a valid email address."

                        is FirebaseAuthException ->
                            "Registration failed: ${exception.message}"

                        else ->
                            "Registration failed. Please try again."
                    }
                    view.onFailure(errorMessage)
                }
            }
    }

    override fun onCameraPermissionGranted() {
        view.captureImageFromCamera()
    }

    override fun onStoragePermissionGranted() {
        view.pickImageFromGallery()
    }
}

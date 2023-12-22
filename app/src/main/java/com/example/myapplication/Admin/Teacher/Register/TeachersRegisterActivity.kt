package com.example.myapplication.Admin.Teacher.Register

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import com.example.myapplication.Admin.AdminDashboardActivity
import com.example.myapplication.databinding.ActivityTeachersRegisterBinding
import java.io.ByteArrayOutputStream

class TeachersRegisterActivity : AppCompatActivity(), TeacherRegisterContract.View {
    private lateinit var binding: ActivityTeachersRegisterBinding
    private lateinit var presenter: TeacherRegisterPresenter

    //    private val CAMERA_PERMISSION_CODE = 101
//    private val STORAGE_PERMISSION_CODE = 102
    private val CAMERA_AND_STORAGE_PERMISSION_CODE = 100
    private val PICK_IMAGE_REQUEST = 103
    private val CAMERA_IMAGE_REQUEST = 104
    private var ImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTeachersRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        presenter = TeacherRegisterPresenter(this)

        binding.imgTeacher.setOnClickListener {
            if (hasCameraPermission() && hasStoragePermission()) {
                // Permissions are already granted, show the image source dialog
                showImageSourceDialog()
            } else {
                // Permissions are not granted, request them
                requestCameraAndStoragePermissions()
            }
        }

        binding.btnRegister.setOnClickListener {
            binding.progressDialog.progressBarLoading.visibility = View.VISIBLE
            binding.btnRegister.visibility = View.GONE

            var name = binding.txtUserNameRegister.text.toString()
            var email = binding.txtUserEmailRegister.text.toString()
            var pass = binding.txtPasswordRegister.text.toString()

            if (ImageUri != null && name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty()) {
                presenter.doRegister(
                    name,
                    email,
                    pass,
                    intent,
                    ImageUri
                )
            } else {
                showToast("Please enter all fields!")
            }
        }
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Change Subject Image")

        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> {
                    // Take Photo (Camera)
                    captureImageFromCamera()
                }

                1 -> {
                    // Choose from Gallery (Storage)
                    pickImageFromGallery()
                }

                2 -> {
                    // Cancel
                    dialog.dismiss()
                }
            }
        }

        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            // Image selected from storage
            ImageUri = data.data
            binding.imgTeacher.setImageURI(ImageUri)
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            // Handle the captured image from the camera
            if (data != null && data.extras != null) {
                val bitmap = data.extras?.get("data") as Bitmap
                ImageUri = getImageUriFromBitmap(bitmap)
                binding.imgTeacher.setImageBitmap(bitmap)
            }
        }
    }


    private fun hasCameraPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasStoragePermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    override fun onTeacherUploaded() {
        binding.progressDialog.progressBarLoading.visibility = View.GONE
        binding.btnRegister.visibility = View.INVISIBLE
        this.finish()
        startActivity(Intent(this@TeachersRegisterActivity, AdminDashboardActivity::class.java))
    }

    override fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun captureImageFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(cameraIntent, CAMERA_IMAGE_REQUEST)
        } else {
            showToast("Camera is not available.")
        }
    }

    override fun requestCameraAndStoragePermissions() {
        val cameraPermission = Manifest.permission.CAMERA
        val storagePermission = Manifest.permission.READ_EXTERNAL_STORAGE

        val permissionsToRequest = mutableListOf<String>()

        if (!hasCameraPermission()) {
            permissionsToRequest.add(cameraPermission)
        }

        if (!hasStoragePermission()) {
            permissionsToRequest.add(storagePermission)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                CAMERA_AND_STORAGE_PERMISSION_CODE
            )
        }
    }

    private fun showToast(message: String) {
        binding.progressDialog.progressBarLoading.visibility = View.GONE
        binding.btnRegister.visibility = View.VISIBLE
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(message: String) {
        showToast(message)
    }

    override fun onFailure(message: String) {
        showToast(message)
    }
}
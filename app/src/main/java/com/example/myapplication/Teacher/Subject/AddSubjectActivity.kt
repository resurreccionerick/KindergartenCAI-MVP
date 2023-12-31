package com.example.myapplication.Teacher.Subject

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.provider.MediaStore
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.myapplication.Teacher.Teacher_Dashboard.TeacherDashboardActivity
import com.example.myapplication.databinding.ActivityAddSubjectBinding
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class AddSubjectActivity : AppCompatActivity(), AddSubjectContract.View {
    private lateinit var binding: ActivityAddSubjectBinding
    private lateinit var presenter: AddSubjectPresenter
    private val CAMERA_PERMISSION_CODE = 101
    private val STORAGE_PERMISSION_CODE = 102
    private val PICK_IMAGE_REQUEST = 103
    private val CAMERA_IMAGE_REQUEST = 104
    private var ImageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSubjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = AddSubjectPresenter(this)


        binding.imgAddSubject.setOnClickListener {
            showImageSourceDialog()
        }

        binding.btnUploadSubject.setOnClickListener {
            binding.progressDialog.progressBarLoading.visibility = VISIBLE
            binding.btnUploadSubject.visibility = INVISIBLE

            val title = binding.txtSubjTitle.text.toString()

            if (title.isNotEmpty() && ImageUri != null) {
                presenter.uploadSubject(intent, title, ImageUri!!)
            } else {
                binding.progressDialog.progressBarLoading.visibility = GONE
                binding.btnUploadSubject.visibility = VISIBLE
                showToast("Please provide a title and select an image.")
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
                    presenter.onCameraPermissionGranted()
                }

                1 -> {
                    // Choose from Gallery (Storage)
                    presenter.onStoragePermissionGranted()
                }

                2 -> {
                    // Cancel
                    dialog.dismiss()
                }
            }
        }

        builder.show()
    }

    override fun requestCameraPermission() {
        requestPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
    }

    override fun requestStoragePermission() {
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE)
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

    private fun requestPermission(permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                CAMERA_PERMISSION_CODE -> presenter.onCameraPermissionGranted()
                STORAGE_PERMISSION_CODE -> presenter.onStoragePermissionGranted()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            // Image selected from storage
            ImageUri = data.data
            binding.imgAddSubject.setImageURI(ImageUri)
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            // Handle the captured image from the camera
            if (data != null && data.extras != null) {
                val bitmap = data.extras?.get("data") as Bitmap
                ImageUri = getImageUriFromBitmap(bitmap)
                binding.imgAddSubject.setImageBitmap(bitmap)
            }
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    override fun onSubjectUploaded() {
        binding.progressDialog.progressBarLoading.visibility = GONE
        binding.btnUploadSubject.visibility = INVISIBLE
        finish()
        startActivity(Intent(this@AddSubjectActivity, TeacherDashboardActivity::class.java))
    }

    override fun showSuccessMessage(message: String) {
        showToast(message)
    }

    override fun showErrorMessage(message: String) {
        binding.btnUploadSubject.visibility = VISIBLE
        showToast(message)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}


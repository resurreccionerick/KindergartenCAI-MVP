package com.example.myapplication.Teacher.Subject

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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.myapplication.Teacher.Teacher_Dashboard.TeacherDashboardActivity
import com.example.myapplication.databinding.ActivityEditSubjectBinding
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class EditSubjectActivity : AppCompatActivity(), EditSubjectContract.View {

    private lateinit var binding: ActivityEditSubjectBinding
    private lateinit var presenter: EditSubjectPresenter
    private val CAMERA_PERMISSION_CODE = 101
    private val STORAGE_PERMISSION_CODE = 102
    private val PICK_IMAGE_REQUEST = 103
    private val CAMERA_IMAGE_REQUEST = 103
    private var ImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSubjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val subjectId = intent.getStringExtra("subjectId")
        val subjectTitle = intent.getStringExtra("subjectTitle")
        val subjectIntentImg = intent.getStringExtra("subjectImg")

        presenter = EditSubjectPresenter(this)

        // Convert the subjectImg string to a Uri
        val subjectImgUri: Uri? = subjectIntentImg?.let { Uri.parse(it) }

        setSubjectDetails(subjectId, subjectTitle, subjectImgUri)

        binding.btnEditUploadSubject.setOnClickListener {
            binding.progressDialog.progressBarLoading.visibility = View.VISIBLE
            binding.btnEditUploadSubject.visibility = View.INVISIBLE

            if (subjectTitle == binding.txtEditSubjTitle.text.toString()) {
                presenter.uploadEditSubject(
                    intent,
                    subjectId,
                    subjectTitle,
                    ImageUri
                ) // Pass the Uri
            } else {
                presenter.uploadEditSubject(
                    intent,
                    subjectId,
                    binding.txtEditSubjTitle.text.toString(),
                    ImageUri
                ) // Pass the Uri
            }
        }

        binding.imgEditSubject.setOnClickListener {
            showImageSourceDialog()
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

    override fun onSubjectUploaded() {
        binding.progressDialog.progressBarLoading.visibility = View.VISIBLE
        binding.btnEditUploadSubject.visibility = View.INVISIBLE
        finish()
        startActivity(Intent(this@EditSubjectActivity, TeacherDashboardActivity::class.java))
    }

    override fun requestCameraPermission() {
        requestPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
    }

    override fun requestStoragePermission() {
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE)
    }

    private fun requestPermission(permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
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
            binding.imgEditSubject.setImageURI(ImageUri)
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            // Handle the captured image from the camera
            if (data != null && data.extras != null) {
                val bitmap = data.extras?.get("data") as Bitmap
                ImageUri = getImageUriFromBitmap(bitmap)
                binding.imgEditSubject.setImageBitmap(bitmap)
            }
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }


    private fun setSubjectDetails(subjectId: String?, subjectTitle: String?, subjectImg: Uri?) {
        binding.txtEditSubjTitle.setText(subjectTitle)
        Picasso.get().load(subjectImg).into(binding.imgEditSubject)
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showSuccessMessage(message: String) {
        showToast(message)
    }

    override fun showErrorMessage(message: String) {
        showToast(message)
    }

    override fun onBackPressed() {
        startActivity(Intent(this, SubjectActivity::class.java))
        finish()
    }
}
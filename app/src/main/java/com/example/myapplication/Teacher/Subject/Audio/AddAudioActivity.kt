package com.example.myapplication.Teacher.Subject.Audio

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.myapplication.R
import com.example.myapplication.Teacher.Teacher_Dashboard.TeacherDashboardActivity
import com.example.myapplication.databinding.ActivityAddAudioBinding
import java.io.ByteArrayOutputStream

class AddAudioActivity : AppCompatActivity(), AddAudioContract.View {

    private lateinit var binding: ActivityAddAudioBinding
    private lateinit var presenter: AddAudioPresenter
    private val CAMERA_PERMISSION_CODE = 101
    private val STORAGE_PERMISSION_CODE = 102
    private val PICK_IMAGE_REQUEST = 103
    private val CAMERA_IMAGE_REQUEST = 104
    private val AUDIO_REQUEST = 105
    private var ImageUri: Uri? = null
    private var AudioUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddAudioBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        presenter = AddAudioPresenter(this)

        binding.imgAddAudio.setOnClickListener {
            showImageSourceDialog()
        }

        binding.btnAddAudio.setOnClickListener {
            presenter.onAudioStoragePermissionGranted()
        }

        binding.btnSaveAudio.setOnClickListener {
            if (binding.txtAddAudio.text.toString()
                    .isNotEmpty() && AudioUri != null && ImageUri != null
            ) {
                presenter.uploadAudio(
                    intent,
                    binding.txtAddAudio.text.toString(),
                    AudioUri!!,
                    ImageUri!!
                )
            } else {
                showToast("Please enter all fields")
            }
        }
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Audio")

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

    override fun onAudioUploaded() {
        binding.progressDialog.progressBarLoading.visibility = View.GONE
        binding.btnSaveAudio.visibility = View.INVISIBLE
        finish()
        startActivity(Intent(this@AddAudioActivity, TeacherDashboardActivity::class.java))
    }

    override fun showSuccessMessage(message: String) {
        showToast(message)
    }

    override fun showErrorMessage(message: String) {
        binding.progressDialog.progressBarLoading.visibility = View.GONE
        binding.btnSaveAudio.visibility = View.VISIBLE

        showToast(message)
    }

    override fun requestCameraPermission() {
        requestPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
    }

    override fun requestStoragePermission() {
        requestPermission(Manifest.permission.CAMERA, STORAGE_PERMISSION_CODE)
    }

    override fun requestAudioStoragePermission() {
        requestPermission(Manifest.permission.RECORD_AUDIO, STORAGE_PERMISSION_CODE)
    }

    override fun pickAudio() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "audio/*"
        startActivityForResult(intent, AUDIO_REQUEST)
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

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            // Image selected from storage
            ImageUri = data.data
            binding.imgAddAudio.setImageURI(ImageUri)
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            // Handle the captured image from the camera
            if (data != null && data.extras != null) {
                val bitmap = data.extras?.get("data") as Bitmap
                ImageUri = getImageUriFromBitmap(bitmap)
                binding.imgAddAudio.setImageBitmap(bitmap)
            }
        } else if (requestCode == AUDIO_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            // Audio file selected
            AudioUri = data.data
            // You can display the selected audio file's name on the button
            val audioName = data.data?.lastPathSegment
            binding.btnAddAudio.text = audioName
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun requestPermission(permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
    }
}
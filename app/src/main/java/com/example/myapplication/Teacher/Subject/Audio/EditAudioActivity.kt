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
import com.example.myapplication.databinding.ActivityEditAudioBinding
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class EditAudioActivity : AppCompatActivity(), EditAudioContract.View {

    private lateinit var binding: ActivityEditAudioBinding
    private lateinit var presenter: EditAudioPresenter
    private val CAMERA_PERMISSION_CODE = 101
    private val STORAGE_PERMISSION_CODE = 102
    private val PICK_IMAGE_REQUEST = 103
    private val CAMERA_IMAGE_REQUEST = 104
    private val AUDIO_REQUEST = 105
    private var ImageUri: Uri? = null
    private var AudioUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEditAudioBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val audioId = intent.getStringExtra("audioId").toString()
        val audioTitle = intent.getStringExtra("audioTitle").toString()
        val audioUri = intent.getStringExtra("audioUrl").toString()
        val audioPicUri = intent.getStringExtra("audioAudioPic").toString()
        val subjId = intent.getStringExtra("subjectID")
        setAudioDetails(audioTitle, audioUri, audioPicUri)

        presenter = EditAudioPresenter(this)

        binding.imgEditAudio.setOnClickListener {
            showImageSourceDialog()
        }

        binding.btnEditAudio.setOnClickListener {
            presenter.onAudioStoragePermissionGranted()
        }

        binding.btnSaveAudio.setOnClickListener {
            if (binding.txtEditAudio.text.toString()
                    .isNotEmpty() && AudioUri != null && ImageUri != null
            ) {
                binding.progressDialog.progressBarLoading.visibility = View.VISIBLE
                binding.btnSaveAudio.visibility = View.INVISIBLE
                binding.btnEditAudio.visibility = View.INVISIBLE

                presenter.uploadEditAudio(
                    audioId,
                    intent,
                    subjId.toString(),
                    binding.txtEditAudio.text.toString(),
                    AudioUri!!,
                    ImageUri!!
                )
            } else {
                showToast("Please enter all fields")
            }
        }
    }

    private fun setAudioDetails(audioTitle: String, audioUri: String, audioPicUri: String) {
        binding.txtEditAudio.setText(audioTitle)
        binding.btnEditAudio.text = audioUri

        Picasso.get().load(audioPicUri).into(binding.imgEditAudio)
    }

    override fun onAudioUploaded() {
        binding.progressDialog.progressBarLoading.visibility = View.GONE
        binding.btnSaveAudio.visibility = View.INVISIBLE
        finish()
        startActivity(Intent(this@EditAudioActivity, TeacherDashboardActivity::class.java))
    }

    override fun showImageSourceDialog() {
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
        val intent = Intent(Intent.ACTION_GET_CONTENT)
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

    override fun showMessage(message: String) {
        showToast(message)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun requestPermission(permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
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
            binding.imgEditAudio.setImageURI(ImageUri)
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            // Handle the captured image from the camera
            if (data != null && data.extras != null) {
                val bitmap = data.extras?.get("data") as Bitmap
                ImageUri = getImageUriFromBitmap(bitmap)
                binding.imgEditAudio.setImageBitmap(bitmap)
            }
        } else if (requestCode == AUDIO_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            // Audio file selected
            AudioUri = data.data
            // You can display the selected audio file's name on the button
            val audioName = data.data?.lastPathSegment
            binding.btnEditAudio.text = audioName
        }
    }
}
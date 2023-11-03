package com.example.myapplication.Teacher.Subject.Video

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.myapplication.R
import com.example.myapplication.Teacher.Teacher_Dashboard.TeacherDashboardActivity
import com.example.myapplication.databinding.ActivityEditVideoBinding

class EditVideoActivity : AppCompatActivity(), EditVideoContract.View {

    private lateinit var binding: ActivityEditVideoBinding
    private lateinit var presenter: EditVideoPresenter
    private val CAMERA_PERMISSION_CODE = 101
    private val STORAGE_PERMISSION_CODE = 102
    private val PICK_VIDEO_REQUEST = 103
    private val CAMERA_VIDEO_REQUEST = 103
    private var videoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val vidId = intent.getStringExtra("videoId").toString()
        val vidTitle = intent.getStringExtra("videoTitle").toString()
        val vidImgUri = intent.getStringExtra("subjectVideo").toString()
        val subjectId = intent.getStringExtra("subjectID")

        // Convert the subjectImg string to a Uri
        val videoToUri: Uri? = vidImgUri?.let { Uri.parse(it) }
        setVideoDetails(vidId, vidTitle, videoToUri)

        //Log.d("ID ERICK", "AddVideoActivity subjID " + subjectId.toString())
        presenter = EditVideoPresenter(this, subjectId.toString())

        binding.fabAddVideo.setOnClickListener {
            binding.fabAddVideo.setOnClickListener {
                showVideoSourceDialog()
            }
        }

        binding.btnAddVideo.setOnClickListener {
            binding.loadingOverlay.visibility = View.VISIBLE // Show the overlay
            binding.progressDialog.progressBarLoading.visibility =
                View.VISIBLE // Show the progress bar

            binding.btnAddVideo.visibility =
                View.GONE // Show the progress bar

            binding.fabAddVideo.visibility = View.GONE

            Log.d(
                "ID ERICK",
                "AddVideoActivity " + binding.txtVideoTitle.text.toString() + " " + vidId.toString()
            )
            if (vidTitle == binding.txtVideoTitle.text.toString()) {

                presenter.uploadEditVideo(intent, vidId, vidTitle, videoUri)
            } else {
                presenter.uploadEditVideo(
                    intent,
                    vidId,
                    binding.txtVideoTitle.text.toString(),
                    videoUri
                )

            }
        }

    }

    private fun setVideoDetails(vidId: Any, vidTitle: Any, vidUri: Uri?) {
        binding.videoViewAddVideo.setVideoURI(vidUri)
        binding.videoViewAddVideo.start() // Start playing the video
        binding.videoViewAddVideo.visibility = View.VISIBLE // Make the VideoView visible

        binding.txtVideoTitle.setText(vidTitle.toString())
    }

    private fun showVideoSourceDialog() {
        val options = arrayOf("Choose from Gallery", "Take Video", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Video Source")

        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> {
                    // Choose from Gallery (Storage)
                    presenter.onStoragePermissionGranted()
                }

                1 -> {
                    // Take Video (Camera)
                    presenter.onCameraPermissionGranted()
                }

                2 -> {
                    // Cancel
                    dialog.dismiss()
                }
            }
        }

        builder.show()
    }

    override fun showSuccessMessage(message: String) {
        showToast(message)
    }

    override fun showErrorMessage(message: String) {
        showToast(message)
    }

    override fun onVideoUploaded() {
        binding.progressDialog.progressBarLoading.visibility = View.GONE
        binding.btnAddVideo.visibility = View.VISIBLE
        finish()
        startActivity(Intent(this@EditVideoActivity, TeacherDashboardActivity::class.java))
    }

    override fun requestCameraPermission() {
        requestPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
    }

    override fun requestStoragePermission() {
        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE)
    }

    private fun requestPermission(permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
    }

    override fun pickVideoFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "video/*"
        startActivityForResult(intent, PICK_VIDEO_REQUEST)
    }

    override fun captureVideoFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(cameraIntent, CAMERA_VIDEO_REQUEST)
        } else {
            showToast("Camera is not available.")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            // Image selected from storage
            videoUri = data.data
            binding.videoViewAddVideo.setVideoURI(videoUri)
        } else if (requestCode == PICK_VIDEO_REQUEST && resultCode == Activity.RESULT_OK) {
            // Handle the captured image from the camera
            if (data != null && data.extras != null) {
                val bitmap = data.extras?.get("data") as Bitmap
                binding.videoViewAddVideo.setVideoURI(videoUri)
            }
        }
    }

    private fun showToast(message: String) {
        binding.loadingOverlay.visibility = View.GONE // Show the overlay
        binding.progressDialog.progressBarLoading.visibility =
            View.GONE // Show the progress bar

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
package com.example.myapplication.Teacher.Subject.Video

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.myapplication.R
import com.example.myapplication.Teacher.Teacher_Dashboard.TeacherDashboardActivity
import com.example.myapplication.databinding.ActivityAddVideoBinding

class AddVideoActivity : AppCompatActivity(), AddVideoContract.View {

    private lateinit var binding: ActivityAddVideoBinding
    private lateinit var presenter: AddVideoPresenter
    private val CAMERA_PERMISSION_CODE = 101
    private val STORAGE_PERMISSION_CODE = 102
    private val PICK_VIDEO_REQUEST = 103
    private val CAMERA_VIDEO_REQUEST = 104
    private var videoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = AddVideoPresenter(this)

        binding.fabAddVideo.setOnClickListener {
            showVideoSourceDialog()
        }

        binding.btnAddVideo.setOnClickListener {
            binding.btnAddVideo.visibility = View.INVISIBLE
            binding.fabAddVideo.visibility = View.INVISIBLE
            binding.progressDialog.progressBarLoading.visibility =
                View.VISIBLE // Show the progress bar


            val title = binding.txtVideoTitle.text.toString()
            val subjectId = intent.getStringExtra("subjectID")

            if (title.isNotEmpty() && videoUri != null) {
                Log.d("ID ", "AddVideoActivity " + subjectId.toString())
                presenter.uploadVideo(intent, subjectId, title, videoUri!!)
            } else {
                showErrorMessage("Please provide a title and select a video.")
            }
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            // Video selected from storage
            videoUri = data.data
            binding.videoViewAddVideo.setVideoURI(videoUri)
            binding.videoViewAddVideo.start() // Start playing the video
            binding.videoViewAddVideo.visibility = View.VISIBLE // Make the VideoView visible
        } else if (requestCode == CAMERA_VIDEO_REQUEST && resultCode == Activity.RESULT_OK) {
            // Handle the captured video from the camera
            videoUri = data?.data
            binding.videoViewAddVideo.setVideoURI(videoUri)
            binding.videoViewAddVideo.start() // Start playing the video
            binding.videoViewAddVideo.visibility = View.VISIBLE // Make the VideoView visible
        }
    }


    override fun onVideoUploaded() {
        binding.btnAddVideo.visibility = View.VISIBLE
        binding.fabAddVideo.visibility = View.VISIBLE
        binding.progressDialog.progressBarLoading.visibility = View.GONE


        finish()
        startActivity(Intent(this@AddVideoActivity, TeacherDashboardActivity::class.java))
    }

    override fun requestCameraPermission() {
        requestPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
    }

    override fun requestStoragePermission() {
        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE)
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

    private fun requestPermission(permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showSuccessMessage(message: String) {
        showToast(message)
    }

    override fun showErrorMessage(message: String) {
        binding.loadingOverlay.visibility = View.GONE // Show the overlay
        binding.progressDialog.progressBarLoading.visibility = View.GONE // Show the progress bar

        showToast(message)
    }
}

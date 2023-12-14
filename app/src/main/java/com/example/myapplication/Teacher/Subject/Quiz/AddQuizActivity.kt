package com.example.myapplication.Teacher.Subject.Quiz

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
import com.example.myapplication.Teacher.Teacher_Dashboard.TeacherDashboardActivity
import com.example.myapplication.databinding.ActivityAddQuizBinding
import java.io.ByteArrayOutputStream

class AddQuizActivity : AppCompatActivity(), AddQuizContract.View {

    private lateinit var binding: ActivityAddQuizBinding
    private lateinit var presenter: AddQuizPresenter
    private val CAMERA_PERMISSION_CODE = 101
    private val STORAGE_PERMISSION_CODE = 102
    private val PICK_IMAGE_REQUEST = 103
    private val CAMERA_IMAGE_REQUEST = 104
    private var ImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddQuizBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val subjID = intent.getStringExtra("subjectID")
        presenter = AddQuizPresenter(intent, subjID.toString(), this)

        binding.imgTitle.setOnClickListener {
            showImageSourceDialog()
        }

        binding.btnSaveQuiz.setOnClickListener {
            binding.progressDialog.progressBarLoading.visibility = View.VISIBLE
            binding.btnSaveQuiz.visibility = View.INVISIBLE

            val title = binding.txtQuestion.text.toString()
            val option1 = binding.txtOpt1.text.toString()
            val option2 = binding.txtOpt2.text.toString()
            val option3 = binding.txtOpt3.text.toString()
            val option4 = binding.txtOpt4.text.toString()

            var correctAns = ""

            if (binding.radioOpt1.isChecked) {
                correctAns = "1"
            } else if (binding.radioOpt2.isChecked) {
                correctAns = "2"
            } else if (binding.radioOpt3.isChecked) {
                correctAns = "3"
            } else if (binding.radioOpt4.isChecked) {
                correctAns = "4"
            }

            if (title.isNotEmpty() || option1.isNotEmpty() || option2.isNotEmpty() || option3.isNotEmpty() || option4.isNotEmpty() || ImageUri != null) {
                presenter.uploadQuiz(
                    intent,
                    title,
                    option1,
                    option2,
                    option3,
                    option4,
                    correctAns,
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

    override fun onQuizUploaded() {
        binding.progressDialog.progressBarLoading.visibility = View.GONE
        binding.btnSaveQuiz.visibility = View.INVISIBLE
        finish()
        startActivity(Intent(this@AddQuizActivity, TeacherDashboardActivity::class.java))
    }

    override fun showSuccessMessage(message: String) {
        showToast(message)
    }

    override fun showErrorMessage(message: String) {
        showToast(message)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            // Image selected from storage
            ImageUri = data.data
            binding.imgTitle.setImageURI(ImageUri)
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            // Handle the captured image from the camera
            if (data != null && data.extras != null) {
                val bitmap = data.extras?.get("data") as Bitmap
                ImageUri = getImageUriFromBitmap(bitmap)
                binding.imgTitle.setImageBitmap(bitmap)
            }
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
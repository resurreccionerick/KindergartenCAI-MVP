package com.example.myapplication.Teacher.Subject.Quiz

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.myapplication.Teacher.Teacher_Dashboard.TeacherDashboardActivity
import com.example.myapplication.databinding.ActivityEditQuizBinding
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class EditQuizActivity : AppCompatActivity(), EditQuizContract.View {

    private lateinit var binding: ActivityEditQuizBinding
    private lateinit var presenter: EditQuizPresenter
    private val CAMERA_PERMISSION_CODE = 101
    private val STORAGE_PERMISSION_CODE = 102
    private val PICK_IMAGE_REQUEST = 103
    private val CAMERA_IMAGE_REQUEST = 104
    private var ImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEditQuizBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //get from teacher quiz activity
        val subjectId = intent.getStringExtra("subjectID")
        val quizId = intent.getStringExtra("quizId")
        val quizTitle = intent.getStringExtra("quizTitle")
        val quizImgTitle = intent.getStringExtra("quizImgTitle")
        val quizOpt1 = intent.getStringExtra("quizOpt1")
        val quizOpt2 = intent.getStringExtra("quizOpt2")
        val quizOpt3 = intent.getStringExtra("quizOpt3")
        val quizOpt4 = intent.getStringExtra("quizOpt4")

        presenter = EditQuizPresenter(subjectId.toString(), quizId.toString(), this)

        // Convert the subjectImg string to a Uri
        val subjectImgUri: Uri? = quizImgTitle?.let { Uri.parse(it) }

        setQuizDetails(quizTitle, subjectImgUri, quizOpt1, quizOpt2, quizOpt3, quizOpt4)

        binding.imgTitle.setOnClickListener {
            showImageSourceDialog()
        }

        binding.btnUpdateQuiz.setOnClickListener {
            binding.btnUpdateQuiz.visibility = View.GONE
            binding.progressDialog.progressBarLoading.visibility = View.VISIBLE

            presenter.uploadEditQuiz(
                intent,
                quizId,
                binding.txtQuestion.text.toString(),
                binding.txtOpt1.text.toString(),
                binding.txtOpt2.text.toString(),
                binding.txtOpt3.text.toString(),
                binding.txtOpt4.text.toString(),
                ImageUri
            )
        }
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Change Quiz Image")

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

    private fun setQuizDetails(
        quizTitle: String?,
        quizImgTitle: Uri?,
        quizOpt1: String?,
        quizOpt2: String?,
        quizOpt3: String?,
        quizOpt4: String?
    ) {
        Picasso.get().load(quizImgTitle).into(binding.imgTitle)

        binding.txtQuestion.setText(quizTitle)
        binding.txtOpt1.setText(quizOpt1)
        binding.txtOpt2.setText(quizOpt2)
        binding.txtOpt3.setText(quizOpt3)
        binding.txtOpt4.setText(quizOpt4)
    }

    override fun showSuccessMessage(message: String) {
        showToast(message)
    }

    override fun showErrorMessage(message: String) {
        binding.btnUpdateQuiz.visibility = View.VISIBLE
        binding.progressDialog.progressBarLoading.visibility = View.GONE
        showToast(message)
    }

    override fun onQuizUploaded() {
        binding.btnUpdateQuiz.visibility = View.VISIBLE
        binding.progressDialog.progressBarLoading.visibility = View.GONE

        finish()
        startActivity(Intent(this@EditQuizActivity, TeacherDashboardActivity::class.java))
    }

    override fun requestCameraPermission() {
        requestPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
    }

    override fun requestStoragePermission() {
        requestPermission(Manifest.permission.CAMERA, STORAGE_PERMISSION_CODE)
    }

    override fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "Image/*"
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
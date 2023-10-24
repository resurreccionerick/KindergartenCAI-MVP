package com.example.myapplication.Teacher.Subject

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.myapplication.Register.RegisterActivity
import com.example.myapplication.Teacher.Subject.AddSubjectContract
import com.example.myapplication.Teacher.Subject.AddSubjectPresenter
import com.example.myapplication.Teacher.Teacher_Dashboard.TeacherDashboardActivity
import com.example.myapplication.databinding.ActivityAddSubjectBinding

class AddSubjectActivity : AppCompatActivity(), AddSubjectContract.View {
    private lateinit var binding: ActivityAddSubjectBinding
    private lateinit var presenter: AddSubjectPresenter
    private val CAMERA_PERMISSION_CODE = 101
    private val STORAGE_PERMISSION_CODE = 102
    private val PICK_IMAGE_REQUEST = 103
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSubjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = AddSubjectPresenter(this)

        binding.imgAddSubject.setOnClickListener {
            presenter.onUploadButtonClick()
        }

        binding.btnUploadSubject.setOnClickListener {
            if (selectedImageUri != null && binding.txtSubjTitle.text.isNotEmpty()) {
                presenter.uploadSubject(binding.txtSubjTitle.text.toString(), selectedImageUri!!)
                binding.progressBarAddSubj.visibility = VISIBLE
            } else {
                showToast("Please select an image and provide a title.")
            }
        }
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
            selectedImageUri = data.data
            binding.imgAddSubject.setImageURI(selectedImageUri)
        }
    }

    override fun onSubjectUploaded() {
        binding.progressBarAddSubj.visibility = INVISIBLE
        finish()
        startActivity(Intent(this@AddSubjectActivity, TeacherDashboardActivity::class.java))
    }

    override fun showSuccessMessage(message: String) {
        showToast(message)
    }

    override fun showErrorMessage(message: String) {
        showToast(message)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}


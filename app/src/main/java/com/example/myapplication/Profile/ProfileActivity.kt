package com.example.myapplication.Profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.Models.User
import com.example.myapplication.Student.Student_Dashboard.StudentDashboardActivity
import com.example.myapplication.databinding.ActivityStudentProfileBinding
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity(), ProfileContract.View {
    private lateinit var binding: ActivityStudentProfileBinding
    private lateinit var presenter: ProfilePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityStudentProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        presenter = ProfilePresenter(this)


        var ref = intent.getStringExtra("userRef").toString()
        presenter.loadProfile(ref)
    }

    override fun setData(user: MutableList<User>) {
        binding.txtProfileName.text = "Name: " + user[0].name
        binding.txtProfileEmail.text = "Email: " + user[0].email
        Picasso.get().load(user[0].img).into(binding.imgProfile)
    }

    override fun showMessage(message: String) {
        showToast(message)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, StudentDashboardActivity::class.java))
        finish()
    }
}
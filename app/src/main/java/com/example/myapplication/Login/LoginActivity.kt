package com.example.myapplication.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.myapplication.Login.Forgot.ForgotPassActivity

import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.Login.Register.RegisterActivity
import com.example.myapplication.Student.Student_Dashboard.StudentDashboardActivity
import com.example.myapplication.Teacher.Teacher_Dashboard.TeacherDashboardActivity


/**
This is an Android activity that implements LoginContract.View.
It inflates a layout (ActivityMainBinding) and sets up the UI elements.
It creates an instance of LoginPresenter and handles button click events.
It also handles success and failure events and displays a toast accordingly. **/
class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var binding: ActivityMainBinding
    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = LoginPresenter(this)
        //presenter.autoLogin() //check if the user was not yet logged out

        binding.btnLogin.setOnClickListener {
            binding.progressBarLogin.visibility = View.VISIBLE
            presenter.doLogin(
                binding.txtLoginEmail.text.toString(),
                binding.txtLoginPass.text.toString()
            )
        }

        binding.btnRegisterLogin.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        binding.btnForgotPass.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPassActivity::class.java))
        }
    }


    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(message: String, isStudent: Boolean) {
        if (isStudent) { //if true
            startActivity(Intent(this@LoginActivity, StudentDashboardActivity::class.java))
        } else {
            startActivity(Intent(this@LoginActivity, TeacherDashboardActivity::class.java))
        }

        showToast(message)
    }

    override fun onFailure(message: String) {
        binding.progressBarLogin.visibility = View.INVISIBLE
        showToast(message)
    }

    override fun autoLogin(message: String) {
        startActivity(Intent(this@LoginActivity, StudentDashboardActivity::class.java))
        showToast(message)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
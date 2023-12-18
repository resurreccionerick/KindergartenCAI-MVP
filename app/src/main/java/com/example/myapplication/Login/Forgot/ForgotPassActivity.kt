package com.example.myapplication.Login.Forgot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.Login.LoginActivity
import com.example.myapplication.R
import com.example.myapplication.Teacher.Teacher_Dashboard.TeacherDashboardActivity
import com.example.myapplication.databinding.ActivityForgotPassBinding

class ForgotPassActivity : AppCompatActivity(), ForgotPassContract.View {

    lateinit var binding: ActivityForgotPassBinding
    lateinit var presenter: ForgotPassPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityForgotPassBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        presenter = ForgotPassPresenter(this)

        binding.btnLogin.setOnClickListener {
            if (binding.txtForgotEmail.text.toString().isNotEmpty()) {
                presenter.forgotPass(binding.txtForgotEmail.text.toString())
            } else {
                showToast("Please enter email address")
            }
        }
    }

    override fun forgotSendSuccess() {
        finish()
        startActivity(Intent(this@ForgotPassActivity, LoginActivity::class.java))
    }

    override fun message(Message: String) {
        showToast(Message)
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@ForgotPassActivity, LoginActivity::class.java))
    }
}
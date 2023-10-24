package com.example.myapplication.Register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.myapplication.Login.LoginActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity(), RegisterContract.View {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var presenter: RegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = RegisterPresenter(this)

        binding.btnRegister.setOnClickListener {
            presenter.doRegister(
                binding.txtUserNameRegister.text.toString(),
                binding.txtPasswordRegister.text.toString()
            )
        }
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(message: String) {
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        showToast(message)
    }

    override fun onFailure(message: String) {
        showToast(message)
    }
}
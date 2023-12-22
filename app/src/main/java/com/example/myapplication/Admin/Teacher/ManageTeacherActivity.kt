package com.example.myapplication.Admin.Teacher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Admin.AdminDashboardActivity
import com.example.myapplication.Admin.Teacher.Register.TeachersRegisterActivity
import com.example.myapplication.Models.User
import com.example.myapplication.databinding.ActivityManagerTeacherBinding

class ManageTeacherActivity : AppCompatActivity(), ManageTeacherContract.View {
    private lateinit var binding: ActivityManagerTeacherBinding
    private lateinit var presenter: ManageTeacherPresenter
    private lateinit var adapter: ManageTeacherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityManagerTeacherBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        presenter = ManageTeacherPresenter(this)

        adapter = ManageTeacherAdapter(emptyList(),
            onActiveCheck = { teacher ->
                presenter.setCheck(teacher)
            })

        binding.rvTeacher.layoutManager = LinearLayoutManager(this)
        binding.rvTeacher.adapter = adapter

        binding.fabRegisterTeacher.setOnClickListener {
            startActivity(Intent(this@ManageTeacherActivity, TeachersRegisterActivity::class.java))
        }

        binding.txtSearchTeacher.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (binding.txtSearchTeacher.text.isNotEmpty()) {
                    adapter.filter(s.toString())
                } else {
                    presenter.loadTeacher()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        presenter.loadTeacher()

    }

    override fun showMessage(message: String) {
        showToast(message)
    }

    override fun setData(user: MutableList<User>) {
        adapter.updateData(user)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        finish()
        startActivity(Intent(this@ManageTeacherActivity, AdminDashboardActivity::class.java))
    }
}
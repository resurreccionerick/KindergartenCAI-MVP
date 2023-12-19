package com.example.myapplication.Admin.Student

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Admin.AdminDashboardActivity
import com.example.myapplication.Admin.Student.Register.StudentRegisterActivity
import com.example.myapplication.Models.User
import com.example.myapplication.databinding.ActivityManageStudentBinding

class ManageStudentActivity : AppCompatActivity(), ManageStudentContract.View {
    private lateinit var binding: ActivityManageStudentBinding
    private lateinit var presenter: ManageStudentPresenter
    private lateinit var adapter: ManageStudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityManageStudentBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        presenter = ManageStudentPresenter(this)

        adapter = ManageStudentAdapter(emptyList(),
            onActiveCheck = { student ->
                presenter.setCheck(student)
            })

        binding.rvStudent.layoutManager = LinearLayoutManager(this)
        binding.rvStudent.adapter = adapter

        binding.fabRegisterStudent.setOnClickListener {
            startActivity(Intent(this@ManageStudentActivity, StudentRegisterActivity::class.java))
        }

        presenter.loadStudent()
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
        startActivity(Intent(this@ManageStudentActivity, AdminDashboardActivity::class.java))
    }
}


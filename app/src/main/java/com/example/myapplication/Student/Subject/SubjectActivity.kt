package com.example.myapplication.Student.Subject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models.Subject
import com.example.myapplication.Student.Student_Dashboard.StudentDashboardActivity
import com.example.myapplication.Student.Subject.SubjectAdapter
import com.example.myapplication.databinding.ActivitySubjectBinding

class SubjectActivity : AppCompatActivity(), SubjectContract.View {
    private lateinit var binding: ActivitySubjectBinding
    private lateinit var presenter: SubjectPresenter
    private lateinit var adapter: SubjectAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySubjectBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        presenter = SubjectPresenter(this)

        // Initialize the RecyclerView and adapter
        val recyclerView: RecyclerView = binding.rvAddSubjList

        adapter = SubjectAdapter(emptyList())

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        // Load subjects
        binding.progressDialog.progressBarLoading.visibility = View.VISIBLE
        presenter.loadSubjects()
    }

    override fun showSubjects(subjects: List<Subject>) {
        // Update the adapter with the list of subjects
        binding.progressDialog.progressBarLoading.visibility = View.GONE
        adapter.updateSubjects(subjects)
    }

    override fun showErrorMessage(message: String) {
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
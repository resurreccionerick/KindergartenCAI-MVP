package com.example.myapplication.Teacher.Subject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models.Subject
import com.example.myapplication.Teacher.Teacher_Dashboard.TeacherDashboardActivity
import com.example.myapplication.databinding.ActivityMainAddSubjectBinding


class SubjectActivity : AppCompatActivity(), SubjectContract.View {
    private lateinit var presenter: SubjectPresenter
    private lateinit var binding: ActivityMainAddSubjectBinding
    private lateinit var adapter: SubjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAddSubjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the presenter
        presenter = SubjectPresenter(this)

        // Initialize the RecyclerView and adapter
        val recyclerView: RecyclerView = binding.rvAddSubjList


        adapter = SubjectAdapter(emptyList(),
            onDeleteClick = { subject ->
                showErrorMessage("Subject deleted: ${subject.title}")
                presenter.deleteSubject(subject)
            },
            onEditClick = { subject ->
                val intent = Intent(this, EditSubjectActivity::class.java)
                intent.putExtra("subjectId", subject.id) // Pass the subject details
                intent.putExtra("subjectTitle", subject.title)
                intent.putExtra("subjectImg", subject.imageUrl)
                startActivity(intent)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        // Load subjects
        binding.progressDialog.progressBarLoading.visibility = View.VISIBLE
        binding.fabAddSubjList.visibility = View.GONE
        presenter.loadSubjects()

        // Set a click listener for adding a new subject
        binding.fabAddSubjList.setOnClickListener {
            startActivity(Intent(this@SubjectActivity, AddSubjectActivity::class.java))
        }
    }

    override fun showSubjects(subjects: List<Subject>) {
        // Update the adapter with the list of subjects
        binding.progressDialog.progressBarLoading.visibility = View.GONE
        binding.fabAddSubjList.visibility = View.VISIBLE
        adapter.updateData(subjects)
    }

    override fun showErrorMessage(message: String) {
        showToast(message)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, TeacherDashboardActivity::class.java))
        finish()
    }
}

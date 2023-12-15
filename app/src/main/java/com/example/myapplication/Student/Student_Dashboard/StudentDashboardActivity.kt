package com.example.myapplication.Student.Student_Dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.myapplication.Login.LoginActivity
import com.example.myapplication.R
import com.example.myapplication.Student.Subject.SubjectActivity
import com.example.myapplication.Teacher.Teacher_Dashboard.TeacherDashboardActivity
import com.example.myapplication.databinding.ActivityStudentdashboardBinding

class StudentDashboardActivity : AppCompatActivity(), StudentDashboardContract.View {
    private lateinit var binding: ActivityStudentdashboardBinding
    private lateinit var presenter: StudentDashboardPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentdashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = StudentDashboardPresenter(this)

        binding

        binding.btnLessonMain.setOnClickListener {
            startActivity(Intent(this@StudentDashboardActivity, SubjectActivity::class.java))
        }

        val toolbar: Toolbar = findViewById(R.id.tbTcherDashboard)
        setSupportActionBar(toolbar) // Set the toolbar as the support action bar
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.tcher_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemProfile -> {
                // Handle action_item1 click
                //presenter.onItem1Clicked() // Notify presenter about the action
                return true
            }

            R.id.itemLogout -> {
                // Handle action_item2 click
                presenter.logout() // Notify presenter about the action
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun logoutSuccess() {
        this.finish()
        startActivity(Intent(this@StudentDashboardActivity, LoginActivity::class.java))
    }

    override fun goToProfile() {
        TODO("Not yet implemented")
    }

    override fun showMessage(message: String) {
        showToast(message)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
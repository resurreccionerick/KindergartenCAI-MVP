package com.example.myapplication.Teacher.Teacher_Dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.myapplication.Login.LoginActivity
import com.example.myapplication.Profile.ProfileActivity
import com.example.myapplication.R
import com.example.myapplication.Teacher.Leaderboards.LeaderboardActivity
import com.example.myapplication.Teacher.Subject.SubjectActivity
import com.example.myapplication.databinding.ActivityTeacherDashboardBinding

class TeacherDashboardActivity : AppCompatActivity(), TeacherDashboardContract.View {

    private lateinit var binding: ActivityTeacherDashboardBinding
    private lateinit var presenter: TeacherDashboardPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = TeacherDashboardPresenter(this)

        binding. btnLeaderboards.setOnClickListener {
            val intent = Intent(this@TeacherDashboardActivity, LeaderboardActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnAddSubj.setOnClickListener {
            val intent = Intent(this@TeacherDashboardActivity, SubjectActivity::class.java)
            startActivity(intent)
            finish()
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
                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra("userRef", "Teachers")
                startActivity(intent)
                finish()
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
        startActivity(Intent(this@TeacherDashboardActivity, LoginActivity::class.java))
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
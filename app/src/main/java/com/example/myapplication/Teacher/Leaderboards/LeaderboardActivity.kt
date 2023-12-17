package com.example.myapplication.Teacher.Leaderboards

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models.User
import com.example.myapplication.Teacher.Teacher_Dashboard.TeacherDashboardActivity
import com.example.myapplication.databinding.ActivityLeaderboardBinding


// LeaderboardActivity.kt

class LeaderboardActivity : AppCompatActivity(), LeaderboardContract.View {
    private lateinit var binding: ActivityLeaderboardBinding
    private lateinit var presenter: LeaderboardPresenter
    private lateinit var usersAdapter: LeaderboardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        presenter = LeaderboardPresenter(this)

        // Initialize RecyclerView and Adapter
        val recyclerView: RecyclerView = binding.rvLeaderboards

        usersAdapter = LeaderboardAdapter(emptyList())

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = usersAdapter

        // Load the leaderboard data
        presenter.loadLeaderboard()
    }

    override fun showMessage(message: String) {
        showToast(message)
    }

    override fun setData(users: List<User>) {
        val sortedUsers = users.sortedByDescending { it.userScore?.toInt() }
        usersAdapter.updateData(sortedUsers)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, TeacherDashboardActivity::class.java))
        finish()
    }
}

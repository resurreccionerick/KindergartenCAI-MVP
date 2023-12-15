package com.example.myapplication.Teacher.Leaderboards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models.User
import com.example.myapplication.databinding.LeaderboardItemBinding

class LeaderboardAdapter(private var user: List<User>) :
    RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {

    inner class LeaderboardViewHolder(val binding: LeaderboardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {

            binding.txtEmail.text = user.userEmail
            binding.txtStar.text = user.userScore
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val binding =
            LeaderboardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeaderboardViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return user.size
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val currentAudio = user[position]
        holder.bind(currentAudio)
    }

    fun updateData(userList: List<User>) {
        user = userList
        notifyDataSetChanged()
    }
}
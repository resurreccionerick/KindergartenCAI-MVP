package com.example.myapplication.Admin.Teacher

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models.User
import com.example.myapplication.databinding.ManageUserItemBinding
import com.squareup.picasso.Picasso


class ManageTeacherAdapter(
    private var users: List<User>,
    private val onActiveCheck: (User) -> Unit
) :
    RecyclerView.Adapter<ManageTeacherAdapter.TeacherViewHolder>() {

    inner class TeacherViewHolder(private val binding: ManageUserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.txtEmail.text = user.email

            // Set the initial state of the switch
            binding.switchActive.isChecked = user.active.equals("true")

            // Set a listener for switch state changes
            binding.switchActive.setOnCheckedChangeListener { _, isChecked ->
                // Update the user's isActive field based on the switch state
                user.active = isChecked.toString()

                // Call the function to update the isActive field in Firebase
                onActiveCheck(user)
            }

            Picasso.get().load(user.img).into(binding.imgUser)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherViewHolder {
        val binding = ManageUserItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TeacherViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {
        holder.bind(users[position])
    }

    fun updateData(userList: List<User>) {
        users = userList
        notifyDataSetChanged()
    }
}



package com.example.myapplication.Student.Leaderboards

import android.util.Log
import com.example.myapplication.Models.User
import com.example.myapplication.Student.Student_Dashboard.StudentDashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LeaderboardPresenter(val view: StudentDashboardActivity) : LeaderboardContract.Presenter {
    private val usersReference = FirebaseDatabase.getInstance().getReference("Users")
    private val auth = FirebaseAuth.getInstance()
    val authId = auth.currentUser?.uid

    override fun loadScore() {
        usersReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user = mutableListOf<User>()
                    // Iterate through all children under "Users"
                    for (userSnapshot in dataSnapshot.children) {

                        val userId = userSnapshot.key // User ID
                        val name = userSnapshot.child("name").getValue(String::class.java)
                        val userEmail = userSnapshot.child("email").getValue(String::class.java)
                        val score = userSnapshot.child("score").getValue(String::class.java)
                        val isActive = userSnapshot.child("active").getValue(String::class.java)
                        val img = userSnapshot.child("img").getValue(String::class.java)

                        if (authId == userId) {
                            val theUser = User(userId.toString(),name, userEmail, score, isActive, img)
                            Log.d("ERICK", theUser.toString())
                            user.add(theUser)
                        }
                    }
                    view.setData(user)
                } else {
                    // No users found under "Users"
                    view.showMessage("No users found")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
                view.showMessage("Error retrieving users: ${databaseError.message}")
            }
        })
    }
}
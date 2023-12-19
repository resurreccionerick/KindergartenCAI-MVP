package com.example.myapplication.Admin.Teacher

import android.util.Log
import com.example.myapplication.Models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ManageTeacherPresenter(val view: ManageTeacherActivity) : ManageTeacherContract.Presenter {
    private val usersReference = FirebaseDatabase.getInstance().getReference("Teachers")

    override fun loadTeacher() {
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

                        val theUser = User(userId.toString(),name, userEmail, score, isActive, img)
                        Log.d("ERICK", theUser.toString())
                        user.add(theUser)

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

    override fun setCheck(student: User) {
        // Get the user ID
        val userId = student.userId

        // Reference to the specific user's node
        val userReference = usersReference.child(userId.toString())

        // Update the isActive field
        userReference.child("active").setValue(student.active)
            .addOnSuccessListener {
                // Handle success
                view.showMessage("Update successful")
            }
            .addOnFailureListener { exception ->
                // Handle error
                view.showMessage("Error updating isActive: ${exception.message}")
            }
    }

}
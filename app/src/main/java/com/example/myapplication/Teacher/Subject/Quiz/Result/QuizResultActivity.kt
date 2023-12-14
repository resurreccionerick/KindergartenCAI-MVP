package com.example.myapplication.Teacher.Subject.Quiz.Result

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.Student.Student_Dashboard.StudentDashboardActivity
import com.example.myapplication.Teacher.Subject.AddSubjectActivity
import com.example.myapplication.databinding.ActivityQuizResultBinding

class QuizResultActivity : AppCompatActivity(), QuizResultContract.View {
    private lateinit var binding: ActivityQuizResultBinding
    private lateinit var presenter: QuizResultPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityQuizResultBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val score = intent.getStringExtra("quizScore") //get from adapter

        presenter = QuizResultPresenter(this)

        binding.txtScore.text = score + "⭐"

        binding.btnNext.setOnClickListener {
            presenter.saveQuizOnDatabase(score.toString())
            binding.btnNext.visibility = View.GONE
            binding.progressDialog.progressBarLoading.visibility = View.VISIBLE
        }

        Glide.with(this)
            .asGif()
            .load(R.drawable.quiz_done)
            .into(binding.img)
    }

    override fun finishSave() {
        binding.progressDialog.progressBarLoading.visibility = View.GONE
        finish()
        startActivity(Intent(this@QuizResultActivity, StudentDashboardActivity::class.java))
    }

    override fun showMessage(message: String) {
        showToast(message)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
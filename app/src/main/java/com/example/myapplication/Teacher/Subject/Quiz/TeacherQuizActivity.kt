package com.example.myapplication.Teacher.Subject.Quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models.Quiz
import com.example.myapplication.Models.Subject
import com.example.myapplication.Teacher.Subject.EditSubjectActivity
import com.example.myapplication.databinding.ActivityTeacherQuizBinding

class TeacherQuizActivity : AppCompatActivity(), QuizContract.View {
    private lateinit var binding: ActivityTeacherQuizBinding
    private lateinit var presenter: QuizPresenter
    private lateinit var adapter: QuizAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTeacherQuizBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val subjectId = intent.getStringExtra("subjectID")

        presenter = QuizPresenter(subjectId.toString(), this)

        presenter.loadQuiz()

        // Initialize the RecyclerView and adapter
        val recyclerView: RecyclerView = binding.rvQuiz

        adapter = QuizAdapter(emptyList(),
            onDeleteClick = { quiz ->
                showMessage("Subject deleted: ${quiz.title}")
                presenter.deleteQuiz(quiz)
            },
            onEditClick = { quiz ->
                val intent = Intent(this, EditQuizActivity::class.java)
                intent.putExtra("subjectID", subjectId)
                intent.putExtra("quizId", quiz.id)
                intent.putExtra("quizTitle", quiz.title)
                intent.putExtra("quizImgTitle", quiz.titleImgUrl)
                intent.putExtra("quizOpt1", quiz.opt1)
                intent.putExtra("quizOpt2", quiz.opt2)
                intent.putExtra("quizOpt3", quiz.opt3)
                intent.putExtra("quizOpt4", quiz.opt4)
                intent.putExtra("correctAns", quiz.correctAns)
                startActivity(intent)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        binding.fabAddQuiz.setOnClickListener {
            val quizIntent = Intent(this@TeacherQuizActivity, AddQuizActivity::class.java)
            quizIntent.putExtra("subjectID", subjectId.toString())
            Log.d("ID", "TeacherQuizAct + " + subjectId.toString())
            binding.root.context.startActivity(quizIntent)
        }
    }


    override fun showQuiz(subjects: List<Quiz>) {
        adapter.updateData(subjects)
    }


    override fun showMessage(message: String) {
        showToast(message)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
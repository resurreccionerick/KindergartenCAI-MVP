package com.example.myapplication.Student.Subject.Quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.myapplication.Models.Question
import com.example.myapplication.Teacher.Subject.Quiz.Result.QuizResultActivity

import com.example.myapplication.databinding.ActivityQuizBinding
import com.squareup.picasso.Picasso

class QuizActivity : AppCompatActivity(), QuizContract.View {
    private lateinit var binding: ActivityQuizBinding
    private lateinit var presenter: QuizPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityQuizBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val subjID = intent.getStringExtra("subjectID")

        presenter = QuizPresenter(this, subjID.toString())

        // Load the first quiz question
        presenter.loadQuizQuestion()

        binding.btnNext.setOnClickListener {
            if (binding.radioBtn1.isChecked) {
                presenter.checkAnswer(1)
            } else if (binding.radioBtn2.isChecked) {
                presenter.checkAnswer(2)
            } else if (binding.radioBtn3.isChecked) {
                presenter.checkAnswer(3)
            } else if (binding.radioBtn4.isChecked) {
                presenter.checkAnswer(4)
            }
        }
    }

    override fun showQuizQuestion(question: Question, currentQuestionIndex: Int, quizSize: Int) {
        var quizIndex = "$currentQuestionIndex/$quizSize"

        // Animate the visibility of the index
        binding.txtIndex.text = quizIndex
        binding.txtIndex.animate()
            .alpha(1f)
            .setDuration(500)
            .setListener(null)


        // Animate the visibility of the image
        if (question.imgTitle.isNotBlank()) {
            binding.imgQuestion.alpha = 0f
            binding.imgQuestion.isVisible = true
            Picasso.get().load(question.imgTitle).into(binding.imgQuestion)
            binding.imgQuestion.animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(null)
        } else {
            binding.imgQuestion.isVisible = false
        }

        // Animate the visibility of the question and radio buttons
        binding.txtQuestion.alpha = 0f
        binding.txtQuestion.text = question.title
        binding.txtQuestion.animate()
            .alpha(1f)
            .setDuration(500)
            .setListener(null)

        // Animate the visibility of radio buttons
        binding.txtQuestion.text = question.title
        binding.radioBtn1.text = question.opt1
        binding.radioBtn2.text = question.opt2
        binding.radioBtn3.text = question.opt3
        binding.radioBtn4.text = question.opt4

        val radioButtons = listOf(
            binding.radioBtn1,
            binding.radioBtn2,
            binding.radioBtn3,
            binding.radioBtn4
        )
        for (radioButton in radioButtons) {
            radioButton.alpha = 0f
            radioButton.animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(null)
        }

        // Reset the RadioGroup selection
        binding.radioGroup.clearCheck()
    }


    override fun showQuizResult(isCorrect: Boolean, score: Int) {
        if (isCorrect) {
            showMessage("Correct!")
            binding.txtScore.text = "Score: $score"
        } else {
            showMessage("Incorrect!")
            binding.txtScore.text = "Score: $score"
        }
    }

    override fun showMessage(message: String) {
        showToast(message)
    }

    override fun finishQuiz(quizScore: String) {
        val intent = Intent(this, QuizResultActivity::class.java)
        intent.putExtra("quizScore", quizScore) // Pass the score
        startActivity(intent)
        finish()
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
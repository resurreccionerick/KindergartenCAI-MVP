package com.example.myapplication.Teacher.Quiz

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var presenter: QuizPresenter

    var answered = false
    var qCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = QuizPresenter(this)

        binding.btnNext.setOnClickListener {
            if (answered) {
                if (binding.radioBtn1.isChecked || binding.radioBtn2.isChecked || binding.radioBtn3.isChecked || binding.radioBtn4.isChecked) {
                    presenter.checkAnswer()
                } else {
                    Toast.makeText(
                        this@QuizActivity,
                        "Please pick your answer.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                qCounter++
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({ presenter.showNextQuestion() }, 500)
            }
        }

    }
}
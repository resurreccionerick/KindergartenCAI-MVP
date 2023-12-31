package com.example.myapplication.Teacher.Subject.Quiz

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models.Quiz
import com.example.myapplication.databinding.QuizItemBinding
import com.squareup.picasso.Picasso

class QuizAdapter(
    private var quizes: List<Quiz>,
    private val onEditClick: (Quiz) -> Unit, //Callback for edit button click
    private val onDeleteClick: (Quiz) -> Unit // Callback for delete button click
) :
    RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    inner class QuizViewHolder(private val binding: QuizItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(quiz: Quiz) {
            binding.txtTitle.text = quiz.title
            binding.txtOpt1.text = quiz.opt1
            binding.txtOpt2.text = quiz.opt2
            binding.txtOpt3.text = quiz.opt3
            binding.txtOpt4.text = quiz.opt4

            Picasso.get().load(quiz.titleImgUrl).into(binding.imgQuestion)

            binding.fabSubjDelete.setOnClickListener {
                onDeleteClick(quiz)
            }

            binding.fabSubjEdit.setOnClickListener {
                onEditClick(quiz)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val binding = QuizItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuizViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return quizes.size
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quiz = quizes[position]
        holder.bind(quiz)
    }

    fun updateData(newQuiz: List<Quiz>) {
        quizes = newQuiz
        notifyDataSetChanged()
    }
}

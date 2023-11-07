package com.example.myapplication.Teacher.Subject

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models.Subject
import com.example.myapplication.databinding.SubjectItemBinding
import com.squareup.picasso.Picasso

class SubjectAdapter(
    private var subjects: List<Subject>,
    private val onEditClick: (Subject) -> Unit, //Callback for edit button click
    private val onDeleteClick: (Subject) -> Unit // Callback for delete button click
) :
    RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>() {

    inner class SubjectViewHolder(private val binding: SubjectItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(subject: Subject) {
            binding.txtTitle.text = subject.title

            Picasso.get().load(subject.imageUrl).into(binding.imgSubject)

            binding.fabSubjDelete.setOnClickListener {
                onDeleteClick(subject)
            }

            binding.fabSubjEdit.setOnClickListener {
                onEditClick(subject)
            }

            binding.cardSubject.setOnClickListener {
                val intent = Intent(binding.root.context, AddLessonActivity::class.java)
                intent.putExtra("subjectID", subject.id)
                Log.d("ID SubjAdapter", "SubjAdapter " + subject.id.toString())
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val binding = SubjectItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SubjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        val subject = subjects[position]
        holder.bind(subject)
    }

    override fun getItemCount() = subjects.size

    fun updateData(newSubjects: List<Subject>) {
        subjects = newSubjects
        notifyDataSetChanged()
    }
}



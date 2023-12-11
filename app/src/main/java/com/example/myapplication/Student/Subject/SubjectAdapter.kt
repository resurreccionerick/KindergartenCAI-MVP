package com.example.myapplication.Student.Subject

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models.Subject
import com.example.myapplication.Teacher.Subject.Video.EditVideoActivity
import com.example.myapplication.databinding.SubjectItemBinding
import com.squareup.picasso.Picasso

class SubjectAdapter(private var subjects: List<Subject>) :
    RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>() {

    inner class SubjectViewHolder(private val binding: SubjectItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(subject: Subject) {
            binding.txtTitle.text = subject.title
            binding.fabSubjEdit.isVisible = false
            binding.fabSubjDelete.isVisible = false
            Picasso.get().load(subject.imageUrl).into(binding.imgSubject)

            binding.cardSubject.setOnClickListener {
                val intent = Intent(binding.root.context, PickLessonActivity::class.java)
                intent.putExtra("subjectID", subject.id)
                Log.d("ID SubjAdapter", "Pick lesson SubjAdapter " + subject.id.toString())
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubjectAdapter.SubjectViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SubjectItemBinding.inflate(inflater, parent, false)
        return SubjectViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return subjects.size
    }

    override fun onBindViewHolder(holder: SubjectAdapter.SubjectViewHolder, position: Int) {
        val subject = subjects[position]
        holder.bind(subject)
    }

    // Add a method to update the subject list when needed
    fun updateSubjects(newSubjects: List<Subject>) {
        subjects = newSubjects
        notifyDataSetChanged()
    }
}

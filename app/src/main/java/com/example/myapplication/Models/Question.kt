package com.example.myapplication.Models

data class Question(
    val title: String,
    val imgTitle:String,
    val opt1: String,
    val opt2: String,
    val opt3: String,
    val opt4: String,
    val correctOption: Int
)

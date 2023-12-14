package com.example.myapplication.Models

data class Quiz(
    var id: String?,
    var title: String,
    var opt1: String,
    var opt2: String,
    var opt3: String,
    var opt4: String,
    var correctAns: String,
    var titleImgUrl: String
) {
}
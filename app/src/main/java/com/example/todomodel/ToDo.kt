package com.example.todomodel

import androidx.compose.Model

@Model
data class ToDo(
    var taskText: String,
    var editing: Boolean,
    var completed: Boolean
)

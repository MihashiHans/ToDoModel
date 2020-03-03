package com.example.todomodel

import androidx.compose.Model
import androidx.compose.frames.ModelList

@Model
class ToDoModel() {
    private val toDoList: ModelList<ToDo> = ModelList()
    val HINT = "What needs to be done?"
    var editingNew = false

    enum class Display {
        All,
        Active,
        Completed
    }

    var display: Display = Display.All

    fun startEditing() {
        editingNew = true
    }

    fun stopEditing() {
        editingNew = false
    }

    fun insert(toDo: ToDo) {
        toDoList.add(toDo)
    }

    fun getItems(): ModelList<ToDo> {
        return toDoList
    }

    fun checkAll() {
        toDoList.takeUnless {
            toDoList.none {
                !it.completed
            }
        }?.filter {
            !it.completed
        }?.map {
            it.completed = true
        } ?: run {
            toDoList.map {
                it.completed = false
            }
        }
    }

    fun getActiveCount(): Int {
        return toDoList.filter {
            !it.completed }.count()
    }

    fun hasCompleted(): Boolean {
        return toDoList.any {
            it.completed
        }
    }

    fun delete(toDo: ToDo) {
        toDoList.remove(toDo)
    }

    fun deleteCompletedToDos() {
        toDoList.filter { it.completed }.map { delete(it) }
    }
}
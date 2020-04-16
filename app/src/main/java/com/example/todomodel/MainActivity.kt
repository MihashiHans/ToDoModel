package com.example.todomodel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.foundation.TextField
import androidx.ui.core.setContent
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.VerticalScroller
import androidx.ui.graphics.Color
import androidx.ui.input.ImeAction
import androidx.ui.input.KeyboardType
import androidx.ui.layout.*
import androidx.ui.material.Button
import androidx.ui.material.Checkbox
import androidx.ui.material.Divider
import androidx.ui.material.TextButton
import androidx.ui.foundation.TextFieldValue
import androidx.ui.text.TextStyle
import androidx.ui.text.style.TextDecoration
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import androidx.ui.unit.sp

class MainActivity : AppCompatActivity() {
    private lateinit var model: ToDoModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        model = ToDoModel()

        setContent {
            Column {
                Row(modifier = Modifier.weight(3f, true)) {
                    Title()
                }
                Row(modifier = Modifier.weight(1f, true)) {
                    EditText(model)
                }
                Divider(color = Color.LightGray)
                Row(modifier = Modifier.weight(10f, true)) {
                    ShowList(model)
                }
                Row(modifier = Modifier.weight(1f, true)) {
                    Footer(model)
                }
            }
        }
    }
}

@Preview
@Composable
fun Title() {
    Text(
            modifier = Modifier.padding(20.dp),
            text = "todos",
            style = TextStyle(color = Color.Red, fontSize = 74.sp)
    )
}

@Composable
fun EditText(model: ToDoModel) {
    Row {
        val editor =
                state { if (model.editingNew) TextFieldValue() else TextFieldValue(model.HINT) }
        TextButton(
                onClick = { model.checkAll() },
                backgroundColor = Color.White,
                contentColor = Color.DarkGray
        ) {
            Text("V")
        }
        TextField(
                value = editor.value,
                onValueChange = {
                    editor.value = it
                },
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search,
                onImeActionPerformed = {
                    if (editor.value.text.isNotEmpty()) {
                        model.insert(ToDo(editor.value.text, false, false))
                        editor.value = TextFieldValue()
                    }
                },
                onFocus = {
                    model.startEditing()
                    editor.value = TextFieldValue("")
                },
                onBlur = {
                    model.stopEditing()
                    editor.value = TextFieldValue(model.HINT)
                },
                textStyle = TextStyle(
                        color = if (model.editingNew) Color.DarkGray else Color.LightGray,
                        fontSize = 20.sp
                )
        )
    }
}


@Composable
fun ShowList(model: ToDoModel) {
    VerticalScroller {
        Column {
            model.getItems().filter {
                when (model.display) {
                    ToDoModel.Display.Active -> !it.completed
                    ToDoModel.Display.Completed -> it.completed
                    ToDoModel.Display.All -> true
                }
            }.map {
                ToDoItem(it, model)
            }
        }
    }
}

@Composable
fun ToDoItem(toDo: ToDo, model: ToDoModel) {
    Row {
        if (!toDo.editing) {
            Checkbox(
                    modifier = Modifier.weight(1f),
                    checked = toDo.completed,
                    onCheckedChange = {
                        toDo.completed = !toDo.completed
                    }
            )
            Clickable(onClick = { toDo.editing = true }) {
                Text(
                        modifier = Modifier.weight(8f),
                        text = toDo.taskText,
                        style = if (toDo.completed)
                            TextStyle(
                                    color = Color.DarkGray,
                                    fontSize = 20.sp,
                                    textDecoration = TextDecoration.LineThrough
                            ) else
                            TextStyle(
                                    color = Color.DarkGray,
                                    fontSize = 20.sp
                            )
                )
            }
            TextButton(
                    modifier = Modifier.weight(1f),
                    onClick = { model.delete(toDo) }
            ) {
                Text("X")
            }
        } else {
            val editor = state { TextFieldValue(toDo.taskText) }
            Spacer(modifier = Modifier.weight(1f))
            TextField(
                    modifier = Modifier.weight(8f),
                    value = editor.value,
                    onValueChange = { editor.value = it },
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                    onImeActionPerformed = {
                        toDo.taskText = editor.value.text
                        toDo.editing = false
                    },
                    onBlur = {
                        toDo.taskText = editor.value.text
                        toDo.editing = false
                    },
                    textStyle = TextStyle(
                            color = Color.DarkGray,
                            fontSize = 20.sp,
                            background = Color.Yellow
                    )
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
    Divider(color = Color.LightGray)
}

@Composable
fun Footer(model: ToDoModel) {
        Row() {
            TextButton(onClick = {}) {
                Text("${model.getActiveCount()} Items left")
            }
            if (model.display == ToDoModel.Display.All) {
                Button(onClick = {}) { Text("All") }
            } else {
                TextButton(onClick = { model.display = ToDoModel.Display.All }) {
                    Text("All")
                }
            }
            if (model.display == ToDoModel.Display.Active) {
                Button(onClick = {}) { Text("Active") }
            } else {
                TextButton(onClick = { model.display = ToDoModel.Display.Active }) {
                    Text("Active")
                }
            }
            if (model.display == ToDoModel.Display.Completed) {
                Button(onClick = {}) { Text("Completed") }
            } else {
                TextButton(onClick = { model.display = ToDoModel.Display.Completed }) {
                    Text("Completed")
                }
            }
            TextButton(
                    onClick = { model.deleteCompletedToDos() }) {
                Text(if (model.hasCompleted()) "Clear Completed" else "")
            }
        }
}

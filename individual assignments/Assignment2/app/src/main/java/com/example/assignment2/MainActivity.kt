package com.example.assignment2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assignment2.data.Course
import com.example.assignment2.ui.CourseViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { CourseManagerApp() }
    }
}

private sealed interface Screen {
    data object List : Screen
    data object Add : Screen
    data class Details(val course: Course) : Screen
    data class Edit(val course: Course) : Screen
}

@Composable
private fun CourseManagerApp(courseViewModel: CourseViewModel = viewModel()) {
    val courses by courseViewModel.courses.collectAsStateWithLifecycle()
    var screen: Screen by remember { mutableStateOf(Screen.List) }

    MaterialTheme {
        when (val currentScreen = screen) {
            Screen.List -> CourseListScreen(
                courses = courses,
                onAdd = { screen = Screen.Add },
                onCourseClick = { screen = Screen.Details(it) }
            )
            Screen.Add -> CourseFormScreen(
                title = "Add course",
                onCancel = { screen = Screen.List },
                onSave = { department, number, location ->
                    courseViewModel.addCourse(department, number, location)
                    screen = Screen.List
                }
            )
            is Screen.Details -> {
                // Look up the current version so edits are reflected while this screen is open.
                val course = courses.firstOrNull { it.id == currentScreen.course.id }
                if (course == null) {
                    screen = Screen.List
                } else {
                    CourseDetailsScreen(
                        course = course,
                        onBack = { screen = Screen.List },
                        onEdit = { screen = Screen.Edit(course) },
                        onDelete = {
                            courseViewModel.deleteCourse(course)
                            screen = Screen.List
                        }
                    )
                }
            }
            is Screen.Edit -> CourseFormScreen(
                title = "Edit course",
                initialCourse = currentScreen.course,
                onCancel = { screen = Screen.Details(currentScreen.course) },
                onSave = { department, number, location ->
                    courseViewModel.updateCourse(currentScreen.course, department, number, location)
                    screen = Screen.Details(currentScreen.course)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CourseListScreen(
    courses: List<Course>,
    onAdd: () -> Unit,
    onCourseClick: (Course) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Course Manager") }) },
        bottomBar = {
            Column(Modifier.padding(16.dp)) {
                Button(onClick = onAdd, modifier = Modifier.fillMaxWidth()) { Text("Add course") }
            }
        }
    ) { padding ->
        if (courses.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) { Text("No courses yet. Add your first course.") }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(courses, key = { it.id }) { course ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onCourseClick(course) },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Text(
                            text = course.name,
                            modifier = Modifier.padding(20.dp),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CourseDetailsScreen(course: Course, onBack: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text("Course details") }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailRow("Department", course.department)
            DetailRow("Course number", course.number)
            DetailRow("Location", course.location)
            Spacer(Modifier.weight(1f))
            Button(onClick = onEdit, modifier = Modifier.fillMaxWidth()) { Text("Edit course") }
            OutlinedButton(onClick = onDelete, modifier = Modifier.fillMaxWidth()) { Text("Delete course") }
            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back to list") }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text(value, style = MaterialTheme.typography.headlineSmall)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CourseFormScreen(
    title: String,
    initialCourse: Course? = null,
    onCancel: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var department by rememberSaveable(initialCourse?.id) { mutableStateOf(initialCourse?.department.orEmpty()) }
    var number by rememberSaveable(initialCourse?.id) { mutableStateOf(initialCourse?.number.orEmpty()) }
    var location by rememberSaveable(initialCourse?.id) { mutableStateOf(initialCourse?.location.orEmpty()) }
    val isValid = department.isNotBlank() && number.isNotBlank() && location.isNotBlank()

    Scaffold(topBar = { TopAppBar(title = { Text(title) }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OutlinedTextField(
                value = department,
                onValueChange = { department = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Department") },
                singleLine = true
            )
            OutlinedTextField(
                value = number,
                onValueChange = { number = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Course number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Location") },
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                OutlinedButton(onClick = onCancel) { Text("Cancel") }
                Spacer(Modifier.width(12.dp))
                Button(onClick = { onSave(department, number, location) }, enabled = isValid) { Text("Save") }
            }
        }
    }
}

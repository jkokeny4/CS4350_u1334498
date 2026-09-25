package com.example.assignment2.ui

import androidx.lifecycle.ViewModel
import com.example.assignment2.data.Course
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** Owns all course data, keeping it independent from Compose recompositions. */
class CourseViewModel : ViewModel() {
    private var nextId = 3L

    private val _courses = MutableStateFlow(
        listOf(
            Course(1, "CS", "4530", "WEB L104"),
            Course(2, "MATH", "2250", "JTB 130")
        )
    )
    val courses: StateFlow<List<Course>> = _courses.asStateFlow()

    fun addCourse(department: String, number: String, location: String) {
        val course = Course(nextId++, department.trim().uppercase(), number.trim(), location.trim())
        _courses.update { it + course }
    }

    fun updateCourse(course: Course, department: String, number: String, location: String) {
        val updated = course.copy(
            department = department.trim().uppercase(),
            number = number.trim(),
            location = location.trim()
        )
        _courses.update { courses -> courses.map { if (it.id == course.id) updated else it } }
    }

    fun deleteCourse(course: Course) {
        _courses.update { courses -> courses.filterNot { it.id == course.id } }
    }
}

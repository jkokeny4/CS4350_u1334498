package com.example.assignment3.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.assignment3.data.Course
import com.example.assignment3.data.CourseRepository
import com.example.assignment3.data.RoomCourseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CourseViewModel(private val repository: CourseRepository) : ViewModel() {
    val courses: StateFlow<List<Course>> = repository.courses.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun addCourse(department: String, number: String, location: String) = viewModelScope.launch {
        repository.addCourse(department, number, location)
    }

    fun updateCourse(course: Course, department: String, number: String, location: String) = viewModelScope.launch {
        repository.updateCourse(course, department, number, location)
    }

    fun deleteCourse(course: Course) = viewModelScope.launch {
        repository.deleteCourse(course)
    }
}

/** Supplies the one repository singleton to every CourseViewModel instance. */
class CourseViewModelFactory private constructor(
    private val repository: CourseRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(CourseViewModel::class.java))
        return CourseViewModel(repository) as T
    }

    companion object {
        @Volatile private var INSTANCE: CourseViewModelFactory? = null

        fun getInstance(context: Context): CourseViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: CourseViewModelFactory(
                    RoomCourseRepository.getInstance(context.applicationContext)
                ).also { INSTANCE = it }
            }
    }
}

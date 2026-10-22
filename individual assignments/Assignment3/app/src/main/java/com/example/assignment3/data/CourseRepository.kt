package com.example.assignment3.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/** Interface separating the ViewModel from the storage implementation. */
interface CourseRepository {
    val courses: Flow<List<Course>>
    suspend fun addCourse(department: String, number: String, location: String)
    suspend fun updateCourse(course: Course, department: String, number: String, location: String)
    suspend fun deleteCourse(course: Course)
}

/** The app-wide repository singleton that mediates all Room access. */
class RoomCourseRepository private constructor(context: Context) : CourseRepository {
    private val courseDao = CourseDatabase.getInstance(context).courseDao()

    override val courses: Flow<List<Course>> = courseDao.observeCourses().map { entities ->
        entities.map { it.toCourse() }
    }

    override suspend fun addCourse(department: String, number: String, location: String) = withContext(Dispatchers.IO) {
        courseDao.insert(CourseEntity(department.cleanDepartment(), number.trim(), location.trim()))
    }

    override suspend fun updateCourse(course: Course, department: String, number: String, location: String) = withContext(Dispatchers.IO) {
        val entity = CourseEntity(department.cleanDepartment(), number.trim(), location.trim())
        entity.id = course.id
        courseDao.update(entity)
    }

    override suspend fun deleteCourse(course: Course) = withContext(Dispatchers.IO) {
        val entity = CourseEntity(course.department, course.number, course.location)
        entity.id = course.id
        courseDao.delete(entity)
    }

    companion object {
        @Volatile private var INSTANCE: RoomCourseRepository? = null

        fun getInstance(context: Context): RoomCourseRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: RoomCourseRepository(context.applicationContext).also { INSTANCE = it }
            }
    }
}

private fun CourseEntity.toCourse() = Course(id, department, number, location)
private fun String.cleanDepartment() = trim().uppercase()

package com.example.assignment2.data

/** A course record is immutable; changes are made by replacing it through the ViewModel. */
data class Course(
    val id: Long,
    val department: String,
    val number: String,
    val location: String
) {
    val name: String
        get() = "$department $number"
}

package com.example.assignment3.data

data class Course(
    val id: Long,
    val department: String,
    val number: String,
    val location: String
) {
    val name: String get() = "$department $number"
}

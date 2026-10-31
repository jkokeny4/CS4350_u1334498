package com.example.assignment4.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fun_facts")
data class FunFact(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val fetchedAt: Long
)

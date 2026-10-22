package com.example.assignment3.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/** Room's persisted representation of a course. */
@Entity(tableName = "courses")
public class CourseEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String department;
    public String number;
    public String location;

    public CourseEntity(String department, String number, String location) {
        this.department = department;
        this.number = number;
        this.location = location;
    }
}

package com.example.budgettracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val description: String,
    val category: String,
    val amount: Double,
    val date: String,
    val startTime: String,
    val endTime: String,
    val photoPath: String? = null
)
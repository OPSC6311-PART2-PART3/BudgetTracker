package com.example.budgettracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey val id: Int = 1,
    val minGoal: Double,
    val maxGoal: Double
)
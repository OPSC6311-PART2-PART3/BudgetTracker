package com.example.budgettracker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: Goal)

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<Category>>

    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate")
    fun getExpensesForPeriod(startDate: String, endDate: String): Flow<List<Expense>>

    @Query("SELECT SUM(amount) FROM expenses WHERE category = :categoryName AND date BETWEEN :startDate AND :endDate")
    fun getTotalForCategory(categoryName: String, startDate: String, endDate: String): Flow<Double?>

    @Query("SELECT * FROM goals LIMIT 1")
    fun getGoal(): Flow<Goal?>
}
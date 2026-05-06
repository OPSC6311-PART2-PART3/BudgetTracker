package com.example.budgettracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgettracker.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var etStartDate: EditText
    private lateinit var etEndDate: EditText
    private lateinit var tvSummary: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etStartDate = findViewById(R.id.et_start_date)
        etEndDate = findViewById(R.id.et_end_date)
        tvSummary = findViewById(R.id.tv_summary)

        findViewById<Button>(R.id.btn_open_expenses).setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

        findViewById<Button>(R.id.btn_open_categories).setOnClickListener {
            startActivity(Intent(this, CategoriesActivity::class.java))
        }

        findViewById<Button>(R.id.btn_open_goals).setOnClickListener {
            startActivity(Intent(this, BudgetGoalsActivity::class.java))
        }

        findViewById<Button>(R.id.btn_fetch).setOnClickListener {
            loadSummary()
        }
    }

    private fun loadSummary() {
        val start = etStartDate.text.toString()
        val end = etEndDate.text.toString()

        if (start.isEmpty() || end.isEmpty()) {
            tvSummary.text = "Please select start and end dates."
            return
        }

        val dao = AppDatabase.getDatabase(this).appDao()
        lifecycleScope.launch(Dispatchers.IO) {
            val expensesFlow = dao.getExpensesForPeriod(start, end)

            expensesFlow.collect { list ->
                val totalSum = list.sumOf { it.amount }
                withContext(Dispatchers.Main) {
                    tvSummary.text = "Total Expenses: $totalSum"
                }
            }
        }
    }
}
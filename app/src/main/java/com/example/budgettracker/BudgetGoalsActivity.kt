package com.example.budgettracker

import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgettracker.data.AppDatabase
import com.example.budgettracker.data.Goal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class BudgetGoalsActivity : AppCompatActivity() {

    private lateinit var minSeekBar: SeekBar
    private lateinit var maxSeekBar: SeekBar
    private lateinit var tvMinGoal: TextView
    private lateinit var tvMaxGoal: TextView
    private var minGoalVal = 0.0
    private var maxGoalVal = 0.0

    private val format = NumberFormat.getCurrencyInstance(Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)

        minSeekBar = findViewById(R.id.min_seek_bar)
        maxSeekBar = findViewById(R.id.max_seek_bar)
        tvMinGoal = findViewById(R.id.tv_min_goal)
        tvMaxGoal = findViewById(R.id.tv_max_goal)

        minSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar, progress: Int, fromUser: Boolean) {
                minGoalVal = progress.toDouble()
                tvMinGoal.text = "Minimum Goal: ${format.format(minGoalVal)}"
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        maxSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar, progress: Int, fromUser: Boolean) {
                maxGoalVal = progress.toDouble()
                tvMaxGoal.text = "Maximum Goal: ${format.format(maxGoalVal)}"
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        findViewById<Button>(R.id.btn_save_goals).setOnClickListener {
            saveGoalsToDb()
        }
    }

    private fun saveGoalsToDb() {
        val dao = AppDatabase.getDatabase(this).appDao()
        lifecycleScope.launch(Dispatchers.IO) {
            dao.insertGoal(Goal(minGoal = minGoalVal, maxGoal = maxGoalVal))
            runOnUiThread {
                Toast.makeText(this@BudgetGoalsActivity, "Goals Saved!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
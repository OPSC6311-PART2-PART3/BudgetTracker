package com.example.budgettracker

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgettracker.data.AppDatabase
import com.example.budgettracker.data.Expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var etDesc: EditText
    private lateinit var etAmount: EditText
    private lateinit var etCategory: EditText
    private lateinit var etDate: EditText
    private lateinit var etStartTime: EditText
    private lateinit var etEndTime: EditText
    private lateinit var tvPhotoPath: TextView
    private var selectedPhotoUri: Uri? = null

    private val photoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedPhotoUri = result.data?.data
            tvPhotoPath.text = selectedPhotoUri?.toString() ?: "Photo Attached"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        etDesc = findViewById(R.id.et_expense_desc)
        etAmount = findViewById(R.id.et_expense_amount)
        etCategory = findViewById(R.id.et_expense_category)
        etDate = findViewById(R.id.et_expense_date)
        etStartTime = findViewById(R.id.et_expense_start_time)
        etEndTime = findViewById(R.id.et_expense_end_time)
        tvPhotoPath = findViewById(R.id.tv_photo_path)

        findViewById<Button>(R.id.btn_pick_photo).setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            photoLauncher.launch(intent)
        }

        findViewById<Button>(R.id.btn_save_expense).setOnClickListener {
            saveExpense()
        }
    }

    private fun saveExpense() {
        val desc = etDesc.text.toString()
        val amountStr = etAmount.text.toString()
        val category = etCategory.text.toString()
        val date = etDate.text.toString()
        val start = etStartTime.text.toString()
        val end = etEndTime.text.toString()

        if (desc.isEmpty() || amountStr.isEmpty() || category.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountStr.toDoubleOrNull() ?: 0.0
        val expense = Expense(
            description = desc,
            category = category,
            amount = amount,
            date = date,
            startTime = start,
            endTime = end,
            photoPath = selectedPhotoUri?.toString()
        )

        val dao = AppDatabase.getDatabase(this).appDao()
        lifecycleScope.launch(Dispatchers.IO) {
            dao.insertExpense(expense)
            runOnUiThread {
                Toast.makeText(this@AddExpenseActivity, "Expense saved!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
package com.example.budgettracker

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgettracker.data.AppDatabase
import com.example.budgettracker.data.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoriesActivity : AppCompatActivity() {

    private lateinit var etCategoryName: EditText
    private lateinit var lvCategories: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val categoryList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        etCategoryName = findViewById(R.id.et_category_name)
        lvCategories = findViewById(R.id.lv_categories)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categoryList)
        lvCategories.adapter = adapter

        findViewById<Button>(R.id.btn_add_category).setOnClickListener {
            saveCategory()
        }

        loadCategories()
    }

    private fun saveCategory() {
        val name = etCategoryName.text.toString().trim()
        if (name.isEmpty()) return

        val dao = AppDatabase.getDatabase(this).appDao()
        lifecycleScope.launch(Dispatchers.IO) {
            dao.insertCategory(Category(name = name))
            withContext(Dispatchers.Main) {
                Toast.makeText(this@CategoriesActivity, "Category Saved", Toast.LENGTH_SHORT).show()
                etCategoryName.text.clear()
                loadCategories()
            }
        }
    }

    private fun loadCategories() {
        val dao = AppDatabase.getDatabase(this).appDao()
        lifecycleScope.launch {
            dao.getAllCategories().collect { categories ->
                categoryList.clear()
                categoryList.addAll(categories.map { it.name })
                adapter.notifyDataSetChanged()
            }
        }
    }
}
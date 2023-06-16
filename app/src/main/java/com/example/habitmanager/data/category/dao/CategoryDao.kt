package com.example.habitmanager.data.category.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.habitmanager.data.category.model.Category

interface CategoryDao {
    suspend fun insert(category: Category)
    suspend fun update(category: Category)
    suspend fun delete(category: Category)
    suspend fun deleteAll()
    suspend fun selectAll(): List<Category>
    fun selectById(id: Int): Category?

}
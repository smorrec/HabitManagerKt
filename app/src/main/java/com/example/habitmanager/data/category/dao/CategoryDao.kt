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
    /*
    @Insert
    suspend fun insert(category: Category): Long

    @Update
    suspend fun update(category: Category): Int

    @Delete
    suspend fun delete(category: Category)

    @Query("DELETE FROM category")
    suspend fun deleteAll()

    @Query("SELECT * FROM category ORDER BY name ASC")
    suspend fun selectAll(): List<Category>

    @Query("SELECT * FROM category WHERE id=:id")
    suspend fun selectById(id: Int): Category
    */

}
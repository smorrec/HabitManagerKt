package com.example.habitmanager.data.category.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Objects
@Entity
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val picture: Int
): Comparable<Category> {

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(other == null || this::class != other::class) return false
        val category = other as Category
        return name.equals(category.name)
    }

    override fun hashCode(): Int {
        return Objects.hash(name)
    }

    override fun compareTo(other: Category): Int {
        if(id < other.id) return -1
        else if(id > other.id) return 1
        return 0
    }
}
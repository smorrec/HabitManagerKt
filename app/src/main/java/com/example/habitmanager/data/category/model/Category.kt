package com.example.habitmanager.data.category.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.IgnoreExtraProperties
import java.util.Objects
@IgnoreExtraProperties
data class Category(
    val id: Int? = null,
    val name: String? = null,
    val picture: Int? = null
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

    override fun toString(): String {
        return name!!
    }

    override fun compareTo(other: Category): Int {
        if(id!! < other.id!!) return -1
        else if(id > other.id) return 1
        return 0
    }
}
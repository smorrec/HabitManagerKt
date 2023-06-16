package com.example.habitmanager.data.category.dao

import android.content.ContentValues.TAG
import android.util.Log
import com.example.habitmanager.data.category.model.Category
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class CategoryDaoImpl: CategoryDao {
    private val database: DatabaseReference = Firebase.database.getReference("categories")
    private val list: ArrayList<Category> = ArrayList()
    private val postListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            list.clear()

            for(snapShot in dataSnapshot.children){
                val category = snapShot.getValue(Category::class.java)
                list.add(category!!)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
        }
    }

    init {
        database.addValueEventListener(postListener)
    }


    override suspend fun insert(category: Category) {
        database.setValue(category.id).addOnCompleteListener {
            database.child(category.id.toString()).setValue(category)
        }
    }

    override suspend fun update(category: Category){
        database.child("categories").child(category.id.toString()).setValue(category)
    }

    override suspend fun delete(category: Category) {
        database.child("categories").child(category.id.toString()).removeValue()
    }

    override suspend fun deleteAll() {
        database.child("categories").removeValue()
    }

    override suspend fun selectAll(): List<Category> {
        return list
    }

    override fun selectById(id: Int): Category? {
        var category :Category? = null
        list.forEach {
            if(it.id == id){
                category = it
            }
        }
        return category
    }
}
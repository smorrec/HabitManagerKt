package com.example.habitmanager.data.category.repository

import com.example.habitmanager.HabitManagerApplication
import com.example.habitmanager.data.base.BaseRepositpory
import com.example.habitmanager.data.category.dao.CategoryDao
import com.example.habitmanager.data.category.model.Category
import com.example.habitmanagerkt.R
import kotlinx.coroutines.launch

class CategoryRepository (
    private val categoryDao: CategoryDao
    ): BaseRepositpory(){

    init{
        initialize()
    }

    private fun initialize(){
        HabitManagerApplication.scope().launch {
            categoryDao.insert(Category(1, "Actividad Fisica", R.drawable.ic_sport))
        }
        HabitManagerApplication.scope().launch {
            categoryDao.insert(Category(2, "Entretenimiento", R.drawable.ic_entertainment));
        }
        HabitManagerApplication.scope().launch {
            categoryDao.insert(Category(3, "Idiomas", R.drawable.ic_language))
        }
    }

    suspend fun getList(): ArrayList<Category>{
        return categoryDao.selectAll() as ArrayList<Category>
    }

    fun getPicture(id: Int): Int{
        var picture = 0
        HabitManagerApplication.scope().launch{
            picture = repositoryAsyncCall {
                categoryDao.selectById(id).picture
            }.await()
        }
        return picture
    }


    suspend fun getName(id: Int): String{
        return categoryDao.selectById(id).name
    }
}
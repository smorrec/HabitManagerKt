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
        //initialize()
    }

    private fun initialize(){
        HabitManagerApplication.scope().launch {
            categoryDao.insert(Category(1, R.string.sport, R.drawable.ic_sport))
        }
        HabitManagerApplication.scope().launch {
            categoryDao.insert(Category(2, R.string.entertainment, R.drawable.ic_entertainment));
        }
        HabitManagerApplication.scope().launch {
            categoryDao.insert(Category(3, R.string.language, R.drawable.ic_language))
        }
        HabitManagerApplication.scope().launch {
            categoryDao.insert(Category(4, R.string.financies, R.drawable.ic_financies))
        }
        HabitManagerApplication.scope().launch {
            categoryDao.insert(Category(5, R.string.nutrition, R.drawable.ic_nutrition))
        }
        HabitManagerApplication.scope().launch {
            categoryDao.insert(Category(6, R.string.personal_health, R.drawable.ic_personal_health))
        }

    }

    suspend fun getList(): ArrayList<Category>{
        return categoryDao.selectAll() as ArrayList<Category>
    }

    fun getPicture(id: Int): Int{
        return categoryDao.selectById(id)!!.picture!!
    }


    fun getName(id: Int): String{
        return HabitManagerApplication.applicationContext().getString(categoryDao.selectById(id)!!.name!!)
    }
}
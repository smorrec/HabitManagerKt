package com.example.habitmanager.data.base

import com.example.habitmanager.HabitManagerApplication
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

open class BaseRepositpory {

    fun <T> repositoryAsyncCall( func: suspend () -> T): Deferred<T> {
        val result = HabitManagerApplication.scope().async {
            return@async func()
        }
        return result
    }

    fun <T> backgroundJob( func: suspend () -> T): Job {
        val result = HabitManagerApplication.scope().launch {
            func()
        }
        return result
    }
}
package com.example.habitmanager.ui.base

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.habitmanager.HabitManagerApplication
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

open class BaseFragment : Fragment() {
    fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    fun showError(error: Int) {
        Toast.makeText(context, resources.getText(error), Toast.LENGTH_SHORT).show()
    }

    fun <T> Fragment.repositoryCall(func: suspend () -> T) = HabitManagerApplication
        .scope()
        .async{
        return@async func()
    }
}
package com.example.habitmanager.utils

sealed class DataState{
    data class Consuming<T>(val value: T): DataState()
    data class Init<T>(val value: T): DataState()
}
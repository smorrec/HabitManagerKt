package com.example.habitmanager.data

sealed class RepositoryResult<T> {
    data class Success<T>(val value: T): RepositoryResult<Any?>()
    data class Error(val exception: Throwable): RepositoryResult<Any?>()
}
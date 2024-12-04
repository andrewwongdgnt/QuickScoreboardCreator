package com.dgnt.quickScoreboardCreator.core.domain.base.repository

import kotlinx.coroutines.flow.Flow

interface Repository<T> {
    fun getAll(): Flow<List<T>>
    suspend fun getById(id: Int): T?
    suspend fun insert(model: T): Long
    suspend fun insert(models: List<T>): List<Long>
    suspend fun delete(model: T)
    suspend fun delete(models: List<T>)
}
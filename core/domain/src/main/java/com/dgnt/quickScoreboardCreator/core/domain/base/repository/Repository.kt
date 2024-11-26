package com.dgnt.quickScoreboardCreator.core.domain.base.repository

import kotlinx.coroutines.flow.Flow

interface Repository<T> {
    fun getAll(): Flow<List<T>>
    suspend fun getById(id: Int): T?
    suspend fun insert(entity: T): Long
    suspend fun insert(entities: List<T>): List<Long>
    suspend fun update(entity: T): Int
    suspend fun update(entities: List<T>): Int
    suspend fun delete(entity: T)
    suspend fun delete(entities: List<T>)
    suspend fun upsert(entity: T)
    suspend fun upsert(entities: List<T>)
}
package com.dgnt.quickScoreboardCreator.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BaseDao<T> {

    fun getAll(): Flow<List<T>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entities: List<T>): List<Long>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(entity: T): Int

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(entities: List<T>): Int

    @Delete
    suspend fun delete(entity: T)

    @Delete
    suspend fun delete(entities: List<T>)
}
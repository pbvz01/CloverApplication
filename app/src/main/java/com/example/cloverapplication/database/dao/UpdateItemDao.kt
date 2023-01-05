package com.example.cloverapplication.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.cloverapplication.database.entity.UpdateItem

@Dao
interface UpdateItemDao {
    @Query("SELECT * FROM log_changed_item_price")
    suspend fun selectAllUpdateItem(): List<UpdateItem>
    //Kotlin + corot + room
    @Insert
    suspend fun insertAllUpdateItem(item: List<UpdateItem>)
}
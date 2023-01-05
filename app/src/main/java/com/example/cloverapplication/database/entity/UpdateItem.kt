package com.example.cloverapplication.database.entity

import android.annotation.SuppressLint
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "log_changed_item_price")
data class UpdateItem(
    @ColumnInfo(name = "old_price") val oldPrice: Double,
    @ColumnInfo(name = "new_price") val newPrice: Double,
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "order_id") val orderId: String,
    @ColumnInfo(name = "item_id") val itemId: String,
) {
    @PrimaryKey(autoGenerate = true) var id: Int? = null

    @SuppressLint("SimpleDateFormat")
    fun getCorrectDate(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return formatter.format(date)
    }
}
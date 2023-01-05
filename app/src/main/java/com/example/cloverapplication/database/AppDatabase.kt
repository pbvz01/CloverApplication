package com.example.cloverapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cloverapplication.database.dao.UpdateItemDao
import com.example.cloverapplication.database.entity.UpdateItem

@Database(entities = [UpdateItem::class], version = 3, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun updateItemDao(): UpdateItemDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}


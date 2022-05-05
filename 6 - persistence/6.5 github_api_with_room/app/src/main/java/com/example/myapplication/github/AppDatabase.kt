package com.example.myapplication.github

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.github.repository.dao.RepoDao
import com.example.myapplication.github.repository.entity.RepoEntity

@Database(entities = [RepoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
}

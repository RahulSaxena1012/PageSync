package com.pagesync.app.di

import android.content.Context
import androidx.room.Room
import com.pagesync.app.data.local.AppDatabase
import com.pagesync.app.data.local.BookDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "pagesync.db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideBookDao(database: AppDatabase): BookDao = database.bookDao()
}


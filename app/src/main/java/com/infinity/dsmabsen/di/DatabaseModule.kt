package com.infinity.dsmabsen.di

import android.content.Context
import androidx.room.Room
import com.infinity.dsmabsen.db.MyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        MyDatabase::class.java,
        "person_database"
    ).fallbackToDestructiveMigration().allowMainThreadQueries().build()

    @Singleton
    @Provides
    fun provideDao(database: MyDatabase) = database.myDao()

//    @Singleton
//    @Provides
//    fun checkOutDao(database: MyDatabase) = database.myCheckOut()
}
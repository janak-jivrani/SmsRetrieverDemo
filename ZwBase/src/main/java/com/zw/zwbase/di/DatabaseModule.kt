package com.zw.zwbase.di

import android.content.Context
import androidx.room.Room
import com.zw.zwbase.data.local.AppDatabase
import com.zw.zwbase.data.local.dao.ChannelDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabase::class.java, "MyDb").build()
    }

    @Provides
    fun provideChannelDao(appDatabase: AppDatabase): ChannelDao {
        return appDatabase.channelDao()
    }
}
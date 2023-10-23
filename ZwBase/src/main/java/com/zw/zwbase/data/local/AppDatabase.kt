package com.zw.zwbase.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zw.zwbase.data.local.dao.ChannelDao
import com.zw.zwbase.domain.Channel

@Database(entities = [Channel::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun channelDao(): ChannelDao
}
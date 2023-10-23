package com.zw.zwbase.data.repositories

import com.zw.zwbase.data.ChannelDataSource
import com.zw.zwbase.domain.Channel
import kotlinx.coroutines.flow.Flow

class ChannelRepository(private val mChannelDataSource: ChannelDataSource) {
    fun getChannelFlow(): Flow<List<Channel>> = mChannelDataSource.getChannelFlow()
}
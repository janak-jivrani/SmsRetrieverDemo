package com.zw.zwbase.data

import com.zw.zwbase.domain.Channel
import kotlinx.coroutines.flow.Flow

interface ChannelDataSource {
    fun getChannelFlow(): Flow<List<Channel>>
}
package com.zw.composetemplate.framework.data

import com.zw.zwbase.data.ChannelDataSource
import com.zw.zwbase.data.local.dao.ChannelDao
import com.zw.zwbase.domain.Channel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn

class ChannelDataSourceImpl(val channelDao: ChannelDao) : ChannelDataSource {
    override fun getChannelFlow(): Flow<List<Channel>> {
        return channelDao.getChannelFlow()
            .flowOn(Dispatchers.IO)
            .distinctUntilChanged()
            /*.map { entities -> entities.map { it.toTask() } }*/
    }
}
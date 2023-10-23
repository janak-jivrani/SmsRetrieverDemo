package com.zw.composetemplate.framework.data

import com.zw.zwbase.data.ChannelDataSource
import com.zw.zwbase.data.local.dao.ChannelDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    fun provideChannelDataSource(channelDao: ChannelDao): ChannelDataSource {
        return ChannelDataSourceImpl(channelDao)
    }
}
package com.zw.zwbase.usecase

import com.zw.zwbase.data.repositories.ChannelRepository
import com.zw.zwbase.domain.Channel
import kotlinx.coroutines.flow.Flow

class GetChannelsUseCase(private val mTaskRepository: ChannelRepository) {
    fun invoke(): Flow<List<Channel>> = mTaskRepository.getChannelFlow()
}
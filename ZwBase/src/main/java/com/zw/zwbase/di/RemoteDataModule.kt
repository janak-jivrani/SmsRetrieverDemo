package com.zw.zwbase.di

import com.zw.zwbase.data.OTPDataSource
import com.zw.zwbase.data.remote.OTPApiInterface
import com.zw.zwbase.data.remote.OTPDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataModule {

    @Provides
    fun provideOTPDataSource(otpApiInterface: OTPApiInterface) : OTPDataSource {
        return OTPDataSourceImpl(otpApiInterface)
    }
}
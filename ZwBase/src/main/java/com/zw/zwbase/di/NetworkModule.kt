/*
 * Copyright © 2022 GFF. All rights reserved.
 *
 * Android Compose Template
 *
 * Created by GFF developers.
 */
package com.zw.zwbase.di

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import com.zw.zwbase.core.SerializeNulls
import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.zw.zwbase.BuildConfig
import com.zw.zwbase.core.Constant
import com.zw.zwbase.core.NetworkConfig
import com.zw.zwbase.data.remote.OTPApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    const val RAW_RETROFIT = "RAW_RETROFIT"
    const val DEFAULT_RETROFIT = "DEFAULT_RETROFIT"
    const val AUTH_RETROFIT = "AUTH_RETROFIT"
    const val AUTH_OKHTTP_CLIENT = "AUTH_OKHTTP_CLIENT"
    const val DEFAULT_OKHTTP_CLIENT = "DEFAULT_OKHTTP_CLIENT"
    const val COUNTRY_RETROFIT = "COUNTRY_RETROFIT"


    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .add(SerializeNulls.JSON_ADAPTER_FACTORY)
            .build()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor { Timber.tag("OkHttp").d(it) }
        if (BuildConfig.DEBUG) logger.level = HttpLoggingInterceptor.Level.BODY
        else logger.level = HttpLoggingInterceptor.Level.BASIC
        return logger
    }

    /*@RequiresApi(Build.VERSION_CODES.O)
    @Provides
    @Named(DEFAULT_OKHTTP_CLIENT)
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        authenticator: AccessTokenAuthenticator,
        chuckInterceptor: ChuckerInterceptor,
        errorLoggingInterceptor: ErrorLoggingInterceptor,
        @FlipperInterceptor flipperOkhttpInterceptor: Interceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .authenticator(authenticator)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(errorLoggingInterceptor)
            .addInterceptor(chuckInterceptor)
            .addNetworkInterceptor(authInterceptor)
            .addNetworkInterceptor(flipperOkhttpInterceptor)
            .connectTimeout(NetworkConfig.connectTimeoutSeconds, TimeUnit.SECONDS)
            .readTimeout(NetworkConfig.readTimeoutSeconds, TimeUnit.SECONDS)
            .writeTimeout(NetworkConfig.writeTimeoutSeconds, TimeUnit.SECONDS)
//            .cache(CoilUtils.createDefaultCache(context))
            .build()

    @Provides
    @Singleton
    fun provideAccessTokenAuthenticator(
        sessionStorage: SessionStorage,
        fireBaseMessagingHelper: FireBaseMessagingHelper,
        userStorage: UserStorage,
        appDatabase: AppDatabase,
        loggingInterceptor: HttpLoggingInterceptor,
        baseUrlStorage: BaseUrlStorage,
        @ApplicationContext context: Context,
    ) = AccessTokenAuthenticator(
        context,
        sessionStorage,
        fireBaseMessagingHelper,
        userStorage,
        appDatabase,
        baseUrlStorage,
        loggingInterceptor
    )*/

    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext context: Context,
        @Named(DEFAULT_OKHTTP_CLIENT) okHttpClient: OkHttpClient,
    ): ImageLoader =
        ImageLoader.Builder(context)
            .okHttpClient { okHttpClient }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .build()
            }.components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()

    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    @Named(AUTH_RETROFIT)
    @Singleton
    fun provideAuthRetrofit(
        @Named(AUTH_OKHTTP_CLIENT) okHttpClient: OkHttpClient,
        moshi: Moshi/*,
        baseUrlStorage: BaseUrlStorage,*/
    ): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory())
            .baseUrl(Constant.BASE_URL/*baseUrlStorage.getBaseUrl()*/)
            .build()

    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    @Named(DEFAULT_RETROFIT)
    @Singleton
    fun provideRetrofit(
        @Named(DEFAULT_OKHTTP_CLIENT) okHttpClient: OkHttpClient,
        moshi: Moshi/*,
        baseUrlStorage: BaseUrlStorage,*/
    ): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory())
            .baseUrl(Constant.BASE_URL/*baseUrlStorage.getBaseUrl()*/)
            .build()

    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    @Named(COUNTRY_RETROFIT)
    @Singleton
    fun provideCountryRetrofit(
        @Named(AUTH_OKHTTP_CLIENT) okHttpClient: OkHttpClient,
        moshi: Moshi,
    ): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory())
            .baseUrl("https://api.ip2loc.com/SGOtbSPnC4kLfteDQGooiA0gcPyzK93o/")
            .build()

    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    @Named(RAW_RETROFIT)
    @Singleton
    fun provideRawRetrofit(@Named(DEFAULT_OKHTTP_CLIENT) okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory())
            .baseUrl(Constant.BASE_URL/*BuildConfig.BASE_URL*/)
            .build()

    @Provides
    @Singleton
    fun provideOTPApiInterface(moshi: Moshi,loggingInterceptor: HttpLoggingInterceptor): OTPApiInterface {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory())
            .baseUrl(Constant.BASE_URL/*BuildConfig.BASE_URL*/)
            .build()
        return retrofit.create(OTPApiInterface::class.java)
    }

    /*@Provides
    @Singleton
    fun provideAuthApiService(@Named(AUTH_RETROFIT) retrofit: Retrofit) =
        retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideUserApiService(@Named(DEFAULT_RETROFIT) retrofit: Retrofit) =
        retrofit.create(UserApiService::class.java)*/
}

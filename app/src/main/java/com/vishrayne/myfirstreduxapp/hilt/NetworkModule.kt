package com.vishrayne.myfirstreduxapp.hilt

import com.vishrayne.myfirstreduxapp.MainActivity
import com.vishrayne.myfirstreduxapp.hilt.service.ProductsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        val duration = 15L
        return OkHttpClient.Builder()
            .connectTimeout(duration, TimeUnit.SECONDS)
            .readTimeout(duration, TimeUnit.SECONDS)
            .writeTimeout(duration, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesProductService(retrofit: Retrofit): ProductsService {
        return retrofit.create(ProductsService::class.java)
    }
}
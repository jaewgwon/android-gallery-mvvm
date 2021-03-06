package com.unifae.gallery.data.api.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

const val PEXELS_API_KEY = "563492ad6f917000010000019d7e610073fc4666b4aad96632132feb"
const val BASE_URL = "https://api.pexels.com/v1/"

@Module
@InstallIn(SingletonComponent::class)
object PexelsClient {

    @Singleton
    @Provides
    fun providePexelsApi(): PexelsApi {
        val requestInterceptor = Interceptor {
            val url = it.request()
                .url()
                .newBuilder()
                .build()

            val request = it.request()
                .newBuilder()
                .url(url)
                .header("Authorization", PEXELS_API_KEY)
                .build()

            return@Interceptor it.proceed(request)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PexelsApi::class.java)
    }
}
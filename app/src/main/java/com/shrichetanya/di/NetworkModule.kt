package com.shrichetanya.di

import android.app.Application
import com.shrichetanya.utils.Constants
import com.shrichetanya.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    /* @Singleton
     @Provides
     fun provideHttpClient(): OkHttpClient {
         return OkHttpClient
             .Builder()
             .readTimeout(15, TimeUnit.SECONDS)
             .connectTimeout(15, TimeUnit.SECONDS)
             .build()
     }
     @Provides
     @Singleton
     fun getHttpLoggingInterceptor(application: Application): HttpLoggingInterceptor {
         val logging = HttpLoggingInterceptor()
 // set your desired log level
         logging.level = HttpLoggingInterceptor.Level.BODY
         return logging
     }
     @Singleton
     @Provides
     fun provideConverterFactory(): GsonConverterFactory =
          GsonConverterFactory.create()

     @Singleton
     @Provides
     fun provideRetrofit(
         okHttpClient: OkHttpClient,
         httpLoggingInterceptor: HttpLoggingInterceptor,
         gsonConverterFactory: GsonConverterFactory
     ): Retrofit {
         return Retrofit.Builder()
             .baseUrl(Constants.BASE_URL)
             .client(okHttpClient)
             .addConverterFactory(gsonConverterFactory)
             .build()
     }

     @Singleton
     @Provides
     fun provideCurrencyService(retrofit: Retrofit): ApiService =
         retrofit.create(ApiService::class.java)*/
    @Provides
    @Singleton
    fun getInterceptor(application: Application): Interceptor {
        val logging = HttpLoggingInterceptor()
// set your desired log level
        logging.level = HttpLoggingInterceptor.Level.BODY
        /* return logging*/
        return Interceptor {
            val request = it.request().newBuilder()

            /* request.addHeader(
                 "Authorization",
                 "Bearer " + (application as AstroGanitApplication).authToken
             )*/
            val actualRequest = request.build()
            it.proceed(actualRequest)


        }
    }

    @Provides
    @Singleton
    fun getHttpLoggingInterceptor(application: Application): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
// set your desired log level
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Singleton
    @Provides
    fun provideHttpClient(
        interceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {

        return OkHttpClient
            .Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }


    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

}
package com.akinci.chatter.common.di

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.akinci.chatter.BuildConfig
import com.akinci.chatter.common.coroutine.CoroutineContextProvider
import com.akinci.chatter.common.network.NetworkChecker
import com.akinci.chatter.common.network.RestConfig
import com.akinci.chatter.common.storage.Preferences
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // live as long as hole application
object AppModule {

    /** Coroutine context provider
     * START
     * **/
    @Provides
    @Singleton
    fun provideCoroutineContext() = CoroutineContextProvider()
    /** END **/

    /** Network Connection Checker Integration
     * START
     * **/
    @Provides
    @Singleton
    fun provideNetworkChecker(@ApplicationContext context: Context) : NetworkChecker = NetworkChecker(context)
    /** END **/

    /** Shared Preferences Integration
     * START
     * **/
    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext context: Context) : Preferences = Preferences(context)
    /** END **/

    /** Retrofit HILT Integrations
     * START
     * **/
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BaseURL

    @Provides
    @BaseURL
    fun provideBaseUrl() = RestConfig.API_BASE_URL

    @Provides
    fun provideMoshiConverterFactory(mosh: Moshi): MoshiConverterFactory = MoshiConverterFactory.create(mosh)

    @Singleton
    @Provides
    fun providesMoshi() = Moshi.Builder().build()

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RestHttpClient

    @Provides
    @Singleton
    @RestHttpClient
    fun provideRestOkHttpClient(
            @ApplicationContext context: Context
    ) : OkHttpClient {
        val builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            // debug logging activated
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY

            //add logging interceptor
            builder.addInterceptor(logger)
        }

        return builder
                .readTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(100, TimeUnit.SECONDS)
                .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
            @RestHttpClient okHttpClient: OkHttpClient,
            @BaseURL baseURL: String,
            converter: MoshiConverterFactory
    ) : Retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .client(okHttpClient)
            .addConverterFactory(converter)
            .build()
    /** END **/


}
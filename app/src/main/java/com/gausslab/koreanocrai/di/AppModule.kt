package com.gausslab.koreanocrai.di

import android.content.Context
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import com.gausslab.koreanocrai.repository.KoreanOCRAIRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providerKoreanOCRAIRepository() = KoreanOCRAIRepository()

    @Singleton
    @Provides
    fun provideNavController(@ApplicationContext context: Context) =
        NavHostController(context).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
            navigatorProvider.addNavigator(DialogNavigator())
        }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
}
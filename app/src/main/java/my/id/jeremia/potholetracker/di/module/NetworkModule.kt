package my.id.jeremia.potholetracker.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import my.id.jeremia.potholetracker.BuildConfig
import my.id.jeremia.potholetracker.data.remote.Networking
import my.id.jeremia.potholetracker.data.remote.apis.login.AuthAPI
import my.id.jeremia.potholetracker.di.qualifier.BaseUrl
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @Singleton
    fun provideApiOkHttpClient(
        @ApplicationContext context: Context,
    ): OkHttpClient = Networking.createOkHttpClient(
        context.cacheDir,
        100 * 1024 * 1024
    )

    @Provides
    @Singleton
    @BaseUrl
    fun provideBaseUrl(): String = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideAuthAPI(
        @BaseUrl baseUrl: String,
        okHttpClient: OkHttpClient
    ): AuthAPI = Networking.createService(
        baseUrl,
        okHttpClient,
        AuthAPI::class.java
    )



}
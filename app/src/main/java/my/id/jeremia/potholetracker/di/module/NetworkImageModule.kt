package my.id.jeremia.potholetracker.di.module

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import my.id.jeremia.potholetracker.data.remote.Networking
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkImageModule {

    @Provides
    @Singleton
    fun provideNetworkImageLoader(
        @ApplicationContext ctx: Context,
    ) : ImageLoader = ImageLoader.Builder(ctx)
        .memoryCache {
            MemoryCache.Builder(ctx)
                .maxSizePercent(0.25)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(ctx.cacheDir.resolve("image_cache"))
                .maxSizePercent(0.02)
                .build()
        }
        .okHttpClient(
            Networking.createOkHttpClientForImageDownload(
                ctx.cacheDir,
                50 * 1024 * 1024,
            )
        )
        .build()

}
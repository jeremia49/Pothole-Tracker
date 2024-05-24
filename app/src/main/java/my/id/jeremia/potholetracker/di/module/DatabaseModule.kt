package my.id.jeremia.potholetracker.di.module

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import my.id.jeremia.potholetracker.data.local.db.DatabaseService
import my.id.jeremia.potholetracker.di.qualifier.DatabaseName
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabaseService(
        @ApplicationContext ctx: Context,
        @DatabaseName databaseName: String,
    ): DatabaseService =
        Room.databaseBuilder(
            ctx,
            DatabaseService::class.java,
            databaseName
        ).build()


    @Provides
    @Singleton
    @DatabaseName
    fun provideDBName():String =  "potholetracker.db"

}
package my.id.jeremia.potholetracker

import android.content.Context
import androidx.room.Room
import my.id.jeremia.potholetracker.Data.InferenceDatabase
import my.id.jeremia.potholetracker.Data.InferenceRepository

object Graph {

    lateinit var database : InferenceDatabase
    
    val inferenceRepository by lazy{
        InferenceRepository(inferenceDataDao = database.inferenceDataDao())
    }

    fun provide(ctx:Context){
        database = Room
            .databaseBuilder(ctx, InferenceDatabase::class.java, "inference.db")
            .fallbackToDestructiveMigration()
            .build()
    }

}
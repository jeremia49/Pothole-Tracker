package my.id.jeremia.potholetracker

import android.content.Context
import android.os.Environment
import androidx.room.Room
import androidx.room.RoomDatabase
import my.id.jeremia.potholetracker.Data.InferenceDatabase
import my.id.jeremia.potholetracker.Data.InferenceRepository
import net.lingala.zip4j.ZipFile
import java.io.File
import java.io.IOException
import java.time.Instant

object Graph {

    lateinit var database : InferenceDatabase
    
    val inferenceRepository by lazy{
        InferenceRepository(inferenceDataDao = database.inferenceDataDao())
    }



    const val THEDATABASE_DATABASE_BACKUP_SUFFIX = "-bkp"
    const val SQLITE_WALFILE_SUFFIX = "-wal"
    const val SQLITE_SHMFILE_SUFFIX = "-shm"
    const val THEDATABASE_DATABASE_NAME= "inference.db"

    fun provide(ctx:Context){
        database = Room
            .databaseBuilder(ctx, InferenceDatabase::class.java, "inference.db")
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .fallbackToDestructiveMigration()
            .build()
    }

    private fun checkpoint() {
        val db = this.database.openHelper.writableDatabase
        db.query("PRAGMA wal_checkpoint;", emptyArray())
        db.query("PRAGMA wal_checkpoint(FULL);", emptyArray())
        db.query("PRAGMA wal_checkpoint(TRUNCATE);", emptyArray())
    }

    fun backupDatabase(context: Context): Int {
        var result = -99
        val dbFile = context.getDatabasePath(THEDATABASE_DATABASE_NAME)

        val dbWalFile = File(dbFile.path + SQLITE_WALFILE_SUFFIX)
        val dbShmFile = File(dbFile.path + SQLITE_SHMFILE_SUFFIX)
        val bkpFile = File(dbFile.path + THEDATABASE_DATABASE_BACKUP_SUFFIX)
        val bkpWalFile = File(bkpFile.path + SQLITE_WALFILE_SUFFIX)
        val bkpShmFile = File(bkpFile.path + SQLITE_SHMFILE_SUFFIX)
        if (bkpFile.exists()) bkpFile.delete()
        if (bkpWalFile.exists()) bkpWalFile.delete()
        if (bkpShmFile.exists()) bkpShmFile.delete()

        checkpoint()

        try {
            dbFile.copyTo(bkpFile,true)
            if (dbWalFile.exists()) dbWalFile.copyTo(bkpWalFile,true)
            if (dbShmFile.exists()) dbShmFile.copyTo(bkpShmFile, true)
            result = 0
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val fileToBackup  : MutableList<File> =  mutableListOf(bkpFile)
        if (dbWalFile.exists()) fileToBackup.add(dbWalFile)
        if (dbShmFile.exists()) fileToBackup.add(dbShmFile)

        val picturesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val downloadDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val ziptimestamp = Instant.now().toString()

        ZipFile(downloadDir!!.absolutePath+"/backup-"+ziptimestamp +".zip")
            .addFiles(fileToBackup.toList())

        ZipFile(downloadDir!!.absolutePath+"/backup-"+ziptimestamp +".zip")
            .addFolder(picturesDir)

        return result
    }

}
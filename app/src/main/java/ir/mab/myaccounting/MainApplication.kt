package ir.mab.myaccounting

import android.app.Application
import androidx.room.Room
import ir.mab.myaccounting.db.AppDatabase

class MainApplication : Application() {

    companion object {
        lateinit var db: AppDatabase
        fun getAppDb(): AppDatabase {
            return db;
        }
    }

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "my-accounting-db"
        ).fallbackToDestructiveMigration().build()
    }
}
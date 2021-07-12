package ir.mab.myaccounting

import android.R
import android.app.Application
import androidx.room.Room
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
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

        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("vazir.ttf")
                            .build()
                    )
                )
                .build()
        )
    }
}
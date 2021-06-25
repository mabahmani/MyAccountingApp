package ir.mab.myaccounting.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ir.mab.myaccounting.dao.CategoryDao
import ir.mab.myaccounting.dao.TransactionCategoryDao
import ir.mab.myaccounting.dao.TransactionDao
import ir.mab.myaccounting.entity.Category
import ir.mab.myaccounting.entity.Transaction
import ir.mab.myaccounting.entity.TransactionCategory

@Database(entities = [Transaction::class, Category::class, TransactionCategory::class], version = 4)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionCategoryDao(): TransactionCategoryDao
}
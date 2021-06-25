package ir.mab.myaccounting.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ir.mab.myaccounting.entity.Transaction
import ir.mab.myaccounting.entity.TransactionWithCategories

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(transactions: Transaction): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg transactions: Transaction)

    @Update
    suspend fun updateAll(vararg transactions: Transaction)

    @Delete
    suspend fun deleteAll(vararg transactions: Transaction)

    @Query("SELECT * FROM `transaction` WHERE isDebt = 1 ORDER BY isPayed,createdAt DESC")
    fun getDebts(): LiveData<List<TransactionWithCategories>>

    @Query("SELECT SUM(cost) FROM `transaction` WHERE isDebt = 1 AND isPayed = 0")
    fun getDebtsSum(): LiveData<Int>

    @Query("SELECT * FROM `transaction` WHERE isDebt = 1 AND createdAt BETWEEN :start AND :end ORDER BY isPayed,createdAt DESC")
    fun getDebtsByDateRange(start: Long, end: Long): LiveData<List<TransactionWithCategories>>


    @Query("SELECT * FROM `transaction` WHERE isDebt = 0 ORDER BY isPayed,createdAt DESC")
    fun getCredits(): LiveData<List<TransactionWithCategories>>

    @Query("SELECT SUM(cost) FROM `transaction` WHERE isDebt = 0 AND isPayed = 0")
    fun getCreditsSum(): LiveData<Int>

    @Query("SELECT * FROM `transaction` WHERE isDebt = 0 AND createdAt BETWEEN :start AND :end ORDER BY isPayed,createdAt DESC")
    fun getCreditsByDateRange(start: Long, end: Long): LiveData<List<TransactionWithCategories>>

}
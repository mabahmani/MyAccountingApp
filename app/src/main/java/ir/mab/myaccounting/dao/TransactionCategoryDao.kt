package ir.mab.myaccounting.dao

import androidx.room.*
import ir.mab.myaccounting.entity.Transaction
import ir.mab.myaccounting.entity.TransactionCategory

@Dao
interface TransactionCategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(transactionCategoryList: List<TransactionCategory>)

    @Query("DELETE FROM TransactionCategory WHERE tId = :tId ")
    suspend fun deleteTransactionCategoriesWithTId(tId: Long)

    @Query("DELETE FROM TransactionCategory WHERE cId = :cId ")
    suspend fun deleteTransactionCategoriesWithCId(cId: Long)
}
package ir.mab.myaccounting.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ir.mab.myaccounting.entity.Category

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg categories: Category)

    @Update
    suspend fun updateAll(vararg categories: Category)

    @Delete
    suspend fun deleteAll(vararg categories: Category)

    @Query("SELECT * FROM `category`")
    fun getCategories(): LiveData<List<Category>>
}
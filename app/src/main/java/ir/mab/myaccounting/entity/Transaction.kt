package ir.mab.myaccounting.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ir.mab.myaccounting.db.AppTypeConverters

@Entity
data class Transaction(
    @PrimaryKey(autoGenerate = true) var transactionId: Long = 0,
    val description: String,
    val cost: Int,
    val createdAt: Long,
    val isDebt: Boolean,
    var isPayed: Boolean,
    ){
    @Ignore
    var categoryId: List<Long>? = null
}

package ir.mab.myaccounting.entity

import androidx.room.Entity

@Entity(primaryKeys = ["tId","cId"])
data class TransactionCategory(
    val tId: Long,
    val cId: Long
)

package ir.mab.myaccounting.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TransactionWithCategories(
    @Embedded
    val transaction: Transaction,
    @Relation(
        parentColumn = "transactionId",
        entity = Category::class,
        entityColumn = "categoryId",
        associateBy = Junction(
            value = TransactionCategory::class,
            parentColumn = "tId",
            entityColumn = "cId"
        )
    )
    val categories: List<Category>
)

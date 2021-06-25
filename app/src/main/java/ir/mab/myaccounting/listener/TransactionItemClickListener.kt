package ir.mab.myaccounting.listener

import ir.mab.myaccounting.entity.Transaction
import ir.mab.myaccounting.entity.TransactionWithCategories

interface TransactionItemClickListener {
    fun onClick(model: TransactionWithCategories)
    fun onPass(model: TransactionWithCategories)
    fun onRestore(model: TransactionWithCategories)
    fun onLongClick(model: TransactionWithCategories)
}
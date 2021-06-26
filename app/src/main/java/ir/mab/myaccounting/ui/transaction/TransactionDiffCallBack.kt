package ir.mab.myaccounting.ui.transaction

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import ir.mab.myaccounting.entity.TransactionWithCategories

class TransactionDiffCallBack(
    private val newTransactionList: MutableList<TransactionWithCategories>,
    private val oldTransactionList: MutableList<TransactionWithCategories>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldTransactionList.size
    }

    override fun getNewListSize(): Int {
        return newTransactionList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTransactionList[oldItemPosition].transaction.transactionId == newTransactionList[newItemPosition].transaction.transactionId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return false
    }
}
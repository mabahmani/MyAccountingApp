package ir.mab.myaccounting.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.mab.myaccounting.MainApplication
import ir.mab.myaccounting.entity.Transaction
import ir.mab.myaccounting.entity.TransactionCategory
import ir.mab.myaccounting.entity.TransactionWithCategories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel: ViewModel() {

    fun insertTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {

            val insertId = MainApplication.db.transactionDao().insert(transaction)

            val transactionCategories = arrayListOf<TransactionCategory>()

            if (!transaction.categoryId.isNullOrEmpty()){
                for (catId in transaction.categoryId!!){
                    val transactionCategory = TransactionCategory(insertId,catId)
                    transactionCategories.add(transactionCategory)
                }

                MainApplication.db.transactionCategoryDao().insertAll(transactionCategories)
            }
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            MainApplication.db.transactionCategoryDao().deleteTransactionCategoriesWithTId(transaction.transactionId)
            MainApplication.db.transactionDao().updateAll(transaction)
            if (!transaction.categoryId.isNullOrEmpty()){
                val transactionCategories = arrayListOf<TransactionCategory>()
                for (catId in transaction.categoryId!!){
                    val transactionCategory = TransactionCategory(transaction.transactionId,catId)
                    transactionCategories.add(transactionCategory)
                }

                MainApplication.db.transactionCategoryDao().insertAll(transactionCategories)
            }
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            MainApplication.db.transactionCategoryDao().deleteTransactionCategoriesWithTId(transaction.transactionId)
            MainApplication.db.transactionDao().deleteAll(transaction)
        }
    }

    fun getDebts(): LiveData<List<TransactionWithCategories>>{
        return MainApplication.db.transactionDao().getDebts();
    }

    fun getDebtsByDateRange(start:Long, end:Long): LiveData<List<TransactionWithCategories>>{
        return MainApplication.db.transactionDao().getDebtsByDateRange(start, end);
    }

    fun getDebtsSum(): LiveData<Int>{
        return MainApplication.db.transactionDao().getDebtsSum();
    }

    fun getCredits(): LiveData<List<TransactionWithCategories>>{
        return MainApplication.db.transactionDao().getCredits();
    }

    fun getCreditsByDateRange(start:Long, end:Long): LiveData<List<TransactionWithCategories>>{
        return MainApplication.db.transactionDao().getCreditsByDateRange(start, end);
    }

    fun getCreditsSum(): LiveData<Int>{
        return MainApplication.db.transactionDao().getCreditsSum();
    }
}
package ir.mab.myaccounting.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.mab.myaccounting.MainApplication.Companion.db
import ir.mab.myaccounting.entity.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {

    fun insertCategory(categoryTitle: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val category = Category(category = categoryTitle)
            db.categoryDao().insertAll(category)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            db.categoryDao().deleteAll(category)
            db.transactionCategoryDao().deleteTransactionCategoriesWithCId(category.categoryId)
        }
    }

    fun getCategories(): LiveData<List<Category>>{
        return db.categoryDao().getCategories();
    }
}
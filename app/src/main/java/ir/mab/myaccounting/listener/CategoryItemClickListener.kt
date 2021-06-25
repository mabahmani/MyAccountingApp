package ir.mab.myaccounting.listener

import ir.mab.myaccounting.entity.Category

interface CategoryItemClickListener {
    fun onClick(model: Category)
    fun onLongClick(model: Category)
}
package ir.mab.myaccounting.ui.bottomsheet

import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import ir.mab.myaccounting.R
import ir.mab.myaccounting.databinding.BottomSheetAddTransactionBinding
import ir.mab.myaccounting.entity.Category
import ir.mab.myaccounting.entity.Transaction
import ir.mab.myaccounting.entity.TransactionWithCategories
import ir.mab.myaccounting.listener.CategoryItemClickListener
import ir.mab.myaccounting.ui.addtransaction.CategoryAdapter
import ir.mab.myaccounting.util.DateFormatter
import ir.mab.myaccounting.vm.CategoryViewModel
import ir.mab.myaccounting.vm.TransactionViewModel
import java.util.*

class AddTransactionBottomSheet(private var transactionWithCategories: TransactionWithCategories?) : BottomSheetDialogFragment(), CategoryItemClickListener {

    lateinit var binding: BottomSheetAddTransactionBinding
    private val transactionViewModel by activityViewModels<TransactionViewModel>()
    private val categoryViewModel by activityViewModels<CategoryViewModel>()
    private var categoryList = listOf<Category>()
    private var selectedCategoryIds = arrayListOf<Long>()
    private var transactionDate: Long = 0
    private var isDebt: Boolean = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.bottom_sheet_add_transaction,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        setupCategories()
        setupDate()

        if (transactionWithCategories != null){
            initData();
        }
    }

    private fun initData() {
        binding.description.setText(transactionWithCategories?.transaction?.description)
        binding.cost.setText(transactionWithCategories?.transaction?.cost!!.toString())
        binding.date.setText(DateFormatter.formatDateDayNameYearMonthDay(transactionWithCategories?.transaction?.createdAt!!))

        if (transactionWithCategories?.transaction?.isDebt!!)
            binding.radioGroup.check(R.id.debt)
        else
            binding.radioGroup.check(R.id.credit)

        binding.addTransaction.text = getString(R.string.update)

        for (cat in transactionWithCategories?.categories!!){
            selectedCategoryIds.add(cat.categoryId)
        }
    }

    private fun setupDate() {
        transactionDate = Date().time
        binding.date.setText(DateFormatter.formatDateDayNameYearMonthDay(transactionDate))
    }

    private fun setupCategories() {
        val categoryAdapter = CategoryAdapter(categoryList, this)
        binding.categoryList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.categoryList.adapter = categoryAdapter
        categoryViewModel.getCategories().observe(viewLifecycleOwner, {
            for (catId in selectedCategoryIds) {
                for (cat in it) {
                    if (catId == cat.categoryId) {
                        cat.selected = true
                        break
                    }
                }
            }
            categoryList = it
            categoryAdapter.list = it
            categoryAdapter.notifyDataSetChanged()
        })
    }

    private fun setOnClickListeners() {

        binding.date.setOnClickListener{
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setTheme(R.style.ThemeDatePicker)
                    .build()

            datePicker.show(parentFragmentManager, null)

            datePicker.addOnPositiveButtonClickListener {
                transactionDate = it
                binding.date.setText(DateFormatter.formatDateDayNameYearMonthDay(transactionDate))
            }
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            run {
                isDebt = checkedId == R.id.debt
            }
        }

        binding.categoryParent.setEndIconOnClickListener {
            if (!binding.category.text.isNullOrEmpty()) {
                categoryViewModel.insertCategory(binding.category.text.toString())
                binding.category.setText("")
            }
        }

        binding.addTransaction.setOnClickListener {
            var valid = true

            if (binding.description.text.isNullOrEmpty()) {
                valid = false
                binding.description.error = getString(R.string.description_is_required_msg)
            }

            if (binding.cost.text.isNullOrEmpty()) {
                valid = false
                binding.cost.error = getString(R.string.cost_is_required_msg)
            }

            if (binding.date.text.isNullOrEmpty()) {
                valid = false
                binding.date.error = getString(R.string.date_is_required_msg)
            }

            if (valid) {
                val transaction = Transaction(
                    description = binding.description.text.toString(),
                    cost = binding.cost.text.toString().toInt(),
                    createdAt = transactionDate,
                    isDebt = isDebt,
                    isPayed = false,
                )
                transaction.categoryId = selectedCategoryIds
                if (transactionWithCategories == null)
                    transactionViewModel.insertTransaction(transaction)
                else{
                    transaction.transactionId = transactionWithCategories?.transaction?.transactionId!!
                    transactionViewModel.updateTransaction(transaction)
                }
                dismiss()
            }
        }
    }

    override fun onClick(model: Category) {
        selectedCategoryIds = arrayListOf()
        for (cat in categoryList) {
            if (cat.selected)
                selectedCategoryIds.add(cat.categoryId)
        }
    }

    override fun onLongClick(model: Category) {
        categoryViewModel.deleteCategory(model)
    }
}
package ir.mab.myaccounting.ui

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import ir.mab.myaccounting.R
import ir.mab.myaccounting.databinding.ActivityMainBinding
import ir.mab.myaccounting.ui.bottomsheet.AddTransactionBottomSheet
import ir.mab.myaccounting.ui.transaction.TransactionPagerAdapter
import ir.mab.myaccounting.vm.CategoryViewModel
import ir.mab.myaccounting.vm.TransactionViewModel
import kotlinx.coroutines.NonCancellable.cancel

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var addTransactionBottomSheet: AddTransactionBottomSheet
    val categoryViewModel by viewModels<CategoryViewModel>()
    companion object{
        var filter: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
        var dateFilter = false
        var categoryFilter = false
        var startDate: Long = 0
        var endDate: Long = 0
        var filterCategoryList: ArrayList<String?> = arrayListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        addTransactionBottomSheet = AddTransactionBottomSheet(null)
        setOnClickListeners()
        setupViewPagerAndTabLayout();
    }

    private fun setupViewPagerAndTabLayout() {
        binding.viewPager.adapter = TransactionPagerAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            if (position == 0){
                tab.text = getString(R.string.debt)
            }
            else{
                tab.text = getString(R.string.credit)
            }
        }.attach()

        binding.bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.dateFilter -> {
                    val datePicker =
                        MaterialDatePicker.Builder.dateRangePicker()
                            .setTitleText("Select range")
                            .setTheme(R.style.ThemeDatePicker)
                            .build()

                    datePicker.show(supportFragmentManager, null)

                    datePicker.addOnPositiveButtonClickListener {
                        startDate = it.first
                        endDate = it.second
                        dateFilter = true
                        filter.value = true
                    }
                    true
                }
                R.id.categoryFilter -> {
                    categoryViewModel.getCategories().observe(this, {
                        val categoryList: Array<String?> = arrayOfNulls(it.size)
                        for ((index,cat) in it.withIndex())
                            categoryList[index] = cat.category

                        filterCategoryList = arrayListOf()
                        MaterialAlertDialogBuilder(this)
                            .setTitle(resources.getString(R.string.filter_category))
                            .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                                // Respond to neutral button press
                            }
                            .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                                categoryFilter = true
                                filter.value = true
                            }
                            .setMultiChoiceItems(categoryList, null
                            ) { dialogInterface: DialogInterface, i: Int, b: Boolean ->
                                if (b)
                                    filterCategoryList.add(categoryList[i])
                                else
                                    filterCategoryList.remove(categoryList[i])
                            }
                            .show()
                    })

                    true
                }
                else -> false
            }
        }
    }

    private fun setOnClickListeners() {
        binding.addNewTransaction.setOnClickListener {
            dateFilter = false
            categoryFilter = false
            filter.value = false
            addTransactionBottomSheet.apply {
                show(supportFragmentManager, null)
            }
        }
    }
}
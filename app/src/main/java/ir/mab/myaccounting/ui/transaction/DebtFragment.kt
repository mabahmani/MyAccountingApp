package ir.mab.myaccounting.ui.transaction

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import ir.mab.myaccounting.databinding.FragmentDebtBinding
import ir.mab.myaccounting.entity.TransactionWithCategories
import ir.mab.myaccounting.listener.TransactionItemClickListener
import ir.mab.myaccounting.ui.MainActivity
import ir.mab.myaccounting.ui.bottomsheet.AddTransactionBottomSheet
import ir.mab.myaccounting.vm.TransactionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat

class DebtFragment : Fragment(), TransactionItemClickListener {
    lateinit var binding: FragmentDebtBinding
    private val transactionViewModel by activityViewModels<TransactionViewModel>()
    lateinit var transactionAdapter: TransactionAdapter
    lateinit var addTransactionBottomSheet: AddTransactionBottomSheet

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDebtBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        getDebts()
        getDebtsSum()
        checkFilter()
        setRefresh()
    }

    private fun setRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            MainActivity.categoryFilter = false
            MainActivity.dateFilter = false
            getDebts()
        }
    }

    private fun checkFilter() {
        MainActivity.filter.observe(viewLifecycleOwner, {
            if (it){
                if (MainActivity.dateFilter && !MainActivity.categoryFilter) {
                    filterByDate()
                } else if (!MainActivity.dateFilter && MainActivity.categoryFilter) {
                    filterByCategory();
                } else if (MainActivity.dateFilter && MainActivity.categoryFilter) {
                    filterByDateAndCategory()
                } else {
                    getDebts()
                }
            }

            else{
                getDebts()
            }
        })
    }

    private fun filterByDateAndCategory() {
        transactionViewModel.getDebtsByDateRange(MainActivity.startDate, MainActivity.endDate)
            .observe(viewLifecycleOwner, {
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default){
                    val filteredTransaction: ArrayList<TransactionWithCategories> = arrayListOf()
                    for (sc in MainActivity.filterCategoryList) {
                        for (tc in it) {
                            for (cat in tc.categories) {
                                if (cat.category.contentEquals(sc.toString())) {
                                    if (!filteredTransaction.contains(tc)) {
                                        filteredTransaction.add(tc)
                                        break
                                    }
                                }
                            }
                        }
                    }
                    requireActivity().runOnUiThread {
                        checkEmpty(filteredTransaction)
                        transactionAdapter.list = filteredTransaction
                        transactionAdapter.notifyDataSetChanged()
                    }
                }
            })
    }

    private fun filterByCategory() {

        transactionViewModel.getDebts().observe(viewLifecycleOwner, {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default){
                val filteredTransaction: ArrayList<TransactionWithCategories> = arrayListOf()
                for (sc in MainActivity.filterCategoryList) {
                    for (tc in it) {
                        for (cat in tc.categories) {
                            if (cat.category.contentEquals(sc.toString())) {
                                if (!filteredTransaction.contains(tc)) {
                                    filteredTransaction.add(tc)
                                    break
                                }
                            }
                        }
                    }
                }
                requireActivity().runOnUiThread {
                    checkEmpty(filteredTransaction)
                    transactionAdapter.list = filteredTransaction
                    transactionAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    private fun filterByDate() {
        transactionViewModel.getDebtsByDateRange(MainActivity.startDate, MainActivity.endDate)
            .observe(viewLifecycleOwner, {
                checkEmpty(it)
                transactionAdapter.list = it
                transactionAdapter.notifyDataSetChanged()
            })
    }

    private fun getDebtsSum() {
        transactionViewModel.getDebtsSum().observe(viewLifecycleOwner, {
            if (it is Int)
                binding.total.text =
                    String.format("%s T", NumberFormat.getNumberInstance().format(it))
            else
                binding.total.text = String.format("%s T", 0)
        })
    }

    private fun getDebts() {
        transactionViewModel.getDebts().observe(viewLifecycleOwner, {
            checkEmpty(it)
            transactionAdapter.list = it
            transactionAdapter.notifyDataSetChanged()
            binding.swipeRefresh.isRefreshing = false
        })
    }

    private fun checkEmpty(it: List<Any>) {
        if (it.isEmpty())
            binding.empty.visibility = VISIBLE
        else
            binding.empty.visibility = GONE
    }

    private fun setupList() {
        transactionAdapter = TransactionAdapter(arrayListOf(), this)
        binding.transactionList.layoutManager = LinearLayoutManager(requireContext())
        binding.transactionList.adapter = transactionAdapter
    }

    override fun onClick(model: TransactionWithCategories) {
        addTransactionBottomSheet = AddTransactionBottomSheet(model)
        addTransactionBottomSheet.show(parentFragmentManager, null)
    }

    override fun onPass(model: TransactionWithCategories) {
        model.transaction.isPayed = true
        transactionViewModel.updateTransaction(model.transaction)
    }

    override fun onRestore(model: TransactionWithCategories) {
        model.transaction.isPayed = false
        transactionViewModel.updateTransaction(model.transaction)
    }

    override fun onLongClick(model: TransactionWithCategories) {
        transactionViewModel.deleteTransaction(model.transaction)
    }
}
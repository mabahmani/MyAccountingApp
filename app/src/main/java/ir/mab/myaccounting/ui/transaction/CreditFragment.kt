package ir.mab.myaccounting.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ir.mab.myaccounting.R
import ir.mab.myaccounting.databinding.FragmentCreditBinding
import ir.mab.myaccounting.entity.TransactionWithCategories
import ir.mab.myaccounting.listener.TransactionItemClickListener
import ir.mab.myaccounting.ui.MainActivity
import ir.mab.myaccounting.ui.bottomsheet.AddTransactionBottomSheet
import ir.mab.myaccounting.vm.TransactionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat

class CreditFragment : Fragment(), TransactionItemClickListener {
    lateinit var binding: FragmentCreditBinding
    private val transactionViewModel by activityViewModels<TransactionViewModel>()
    lateinit var transactionAdapter: TransactionAdapter
    lateinit var addTransactionBottomSheet: AddTransactionBottomSheet
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreditBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        getCredits()
        getCreditsSum()
        checkFilter()
        setRefresh()
    }

    private fun setRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            MainActivity.categoryFilter = false
            MainActivity.dateFilter = false
            getCredits()
        }
    }

    private fun checkFilter() {
        MainActivity.filter.observe(viewLifecycleOwner, {
            if (it) {
                if (MainActivity.dateFilter && !MainActivity.categoryFilter) {
                    filterByDate()
                } else if (!MainActivity.dateFilter && MainActivity.categoryFilter) {
                    filterByCategory();
                } else if (MainActivity.dateFilter && MainActivity.categoryFilter) {
                    filterByDateAndCategory()
                } else {
                    getCredits()
                }
            } else {
                getCredits()
            }
        })
    }

    private fun filterByDateAndCategory() {
        transactionViewModel.getCreditsByDateRange(MainActivity.startDate, MainActivity.endDate)
            .observe(viewLifecycleOwner, {
                if (MainActivity.dateFilter && MainActivity.categoryFilter) {
                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
                        val filteredTransaction: ArrayList<TransactionWithCategories> =
                            arrayListOf()
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
                            transactionAdapter.updateTransactions(filteredTransaction)
//                            transactionAdapter.list = filteredTransaction
//                            transactionAdapter.notifyDataSetChanged()
                        }
                    }
                }

            })
    }

    private fun filterByCategory() {

        transactionViewModel.getCredits().observe(viewLifecycleOwner, {
            if (MainActivity.categoryFilter) {
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
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
                        transactionAdapter.updateTransactions(filteredTransaction)
//                        transactionAdapter.list = filteredTransaction
//                        transactionAdapter.notifyDataSetChanged()
                    }
                }

            }
        })
    }

    private fun filterByDate() {
        transactionViewModel.getCreditsByDateRange(MainActivity.startDate, MainActivity.endDate)
            .observe(viewLifecycleOwner, {
                if (MainActivity.dateFilter) {
                    checkEmpty(it)
                    transactionAdapter.updateTransactions(it.toMutableList())
//                    transactionAdapter.list = it
//                    transactionAdapter.notifyDataSetChanged()
                }

            })
    }

    private fun getCreditsSum() {
        transactionViewModel.getCreditsSum().observe(viewLifecycleOwner, {
            if (it is Int)
                binding.total.text =
                    String.format("%s T", NumberFormat.getNumberInstance().format(it))
            else
                binding.total.text = String.format("%s T", 0)
        })
    }

    private fun getCredits() {
        transactionViewModel.getCredits().observe(viewLifecycleOwner, {
            checkEmpty(it)
            transactionAdapter.updateTransactions(it.toMutableList())
//            transactionAdapter.list = it
//            transactionAdapter.notifyDataSetChanged()
            binding.swipeRefresh.isRefreshing = false
        })
    }

    private fun checkEmpty(it: List<Any>) {
        if (it.isEmpty())
            binding.empty.visibility = View.VISIBLE
        else
            binding.empty.visibility = View.GONE
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
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.confirm_delete_msg))
            .setNeutralButton(getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                run {
                    transactionViewModel.deleteTransaction(
                        model.transaction
                    )
                }
            }
            .show()
    }

}
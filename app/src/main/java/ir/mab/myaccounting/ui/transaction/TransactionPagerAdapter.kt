package ir.mab.myaccounting.ui.transaction

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TransactionPagerAdapter(fm: FragmentManager, lifeCycle:Lifecycle): FragmentStateAdapter(fm,lifeCycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                DebtFragment()
            }
            else -> {
                CreditFragment()
            }
        }
    }

}
package ir.mab.myaccounting.ui.transaction

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ir.mab.myaccounting.R
import ir.mab.myaccounting.databinding.ItemTransactionBinding
import ir.mab.myaccounting.entity.TransactionWithCategories
import ir.mab.myaccounting.listener.TransactionItemClickListener
import ir.mab.myaccounting.util.DateFormatter
import java.text.NumberFormat

class TransactionAdapter(
    var list: List<TransactionWithCategories>,
    private val transactionItemClickListener: TransactionItemClickListener
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTransactionBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])

        holder.binding.action.setOnClickListener {
            if (list[position].transaction.isPayed)
                transactionItemClickListener.onRestore(list[position])
            else
                transactionItemClickListener.onPass(list[position])
        }

        holder.binding.card.setOnClickListener {
            transactionItemClickListener.onClick(list[position])
        }

        holder.binding.card.setOnLongClickListener {
            transactionItemClickListener.onLongClick(list[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: ItemTransactionBinding) : RecyclerView.ViewHolder(itemView.root) {
        var binding: ItemTransactionBinding = itemView

        init {
            itemView.executePendingBindings()
        }

        fun bind(t: TransactionWithCategories) {
            binding.description.text = t.transaction.description
            binding.date.text = DateFormatter.formatDateDayNameYearMonthDay(t.transaction.createdAt)
            binding.cost.text =
                String.format("%s T", NumberFormat.getNumberInstance().format(t.transaction.cost))
            if (t.transaction.isPayed) {
                binding.action.text = binding.root.context.resources.getString(R.string.restore)
                binding.description.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.blue_grey_50
                    )
                )
                binding.date.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.blue_grey_50
                    )
                )
                binding.cost.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.blue_grey_50
                    )
                )
                binding.categoryList.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.blue_grey_50
                    )
                )
                binding.action.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.blue_grey_50
                    )
                )
                binding.card.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
                binding.card.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
                binding.descriptionIcon.setColorFilter(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.blue_grey_50
                    )
                )
                binding.dateIcon.setColorFilter(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.blue_grey_50
                    )
                )
                binding.categoryIcon.setColorFilter(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.blue_grey_50
                    )
                )
            } else {
                binding.action.text = binding.root.context.resources.getString(R.string.pass)
                binding.description.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.blue_grey_900
                    )
                )
                binding.date.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.blue_grey_900
                    )
                )
                binding.cost.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.blue_grey_900
                    )
                )
                binding.categoryList.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.blue_grey_900
                    )
                )
                binding.action.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.blue_grey_900
                    )
                )
                binding.card.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.grey_50
                    )
                )
                binding.card.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.grey_50
                    )
                )
                binding.descriptionIcon.setColorFilter(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.blue_grey_900
                    )
                )
                binding.dateIcon.setColorFilter(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.blue_grey_900
                    )
                )
                binding.categoryIcon.setColorFilter(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.blue_grey_900
                    )
                )
            }
            if (t.categories.isEmpty()) {
                binding.categoryListParent.visibility = GONE
            } else {
                binding.categoryListParent.visibility = VISIBLE
                var categoriesString = ""
                for ((index, cat) in t.categories.withIndex()) {
                    categoriesString += cat.category
                    if (index != t.categories.size - 1)
                        categoriesString += ", "
                }

                binding.categoryList.text = categoriesString
            }
        }
    }

}
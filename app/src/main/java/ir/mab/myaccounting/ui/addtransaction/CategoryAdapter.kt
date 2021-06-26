package ir.mab.myaccounting.ui.addtransaction

import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ir.mab.myaccounting.R
import ir.mab.myaccounting.databinding.ItemCategoryBinding
import ir.mab.myaccounting.entity.Category
import ir.mab.myaccounting.listener.CategoryItemClickListener

class CategoryAdapter(
    var list: List<Category>,
    var categoryItemClickListener: CategoryItemClickListener
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])

        holder.binding.parent.setOnClickListener {
            list[position].selected = !list[position].selected
            categoryItemClickListener.onClick(list[position])
            notifyItemChanged(position)
        }

        holder.binding.parent.setOnLongClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            categoryItemClickListener.onLongClick(list[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: ItemCategoryBinding) : RecyclerView.ViewHolder(itemView.root) {
        var binding: ItemCategoryBinding = itemView;

        init {
            itemView.executePendingBindings()
        }

        fun bind(c: Category) {
            binding.category.text = c.category

            if (c.selected){
                binding.parent.setCardBackgroundColor(ContextCompat.getColor(binding.root.context,R.color.blue_grey_200))
            }
            else{
                binding.parent.setCardBackgroundColor(ContextCompat.getColor(binding.root.context,R.color.blue_grey_50))
            }
        }
    }
}
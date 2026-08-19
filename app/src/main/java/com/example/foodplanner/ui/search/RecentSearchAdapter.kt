package com.example.foodplanner.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodplanner.databinding.ItemRecentSearchBinding

/**
 * RecentSearchAdapter — Engineer 1
 * Shows recent search terms with X button to remove each entry
 * Course ref: RecyclerView.Adapter p330-335
 */
class RecentSearchAdapter(
    private val searches: MutableList<String>,
    private val onRemove: (String) -> Unit
) : RecyclerView.Adapter<RecentSearchAdapter.RecentSearchVH>() {

    inner class RecentSearchVH(val binding: ItemRecentSearchBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount() = searches.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RecentSearchVH(
            ItemRecentSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: RecentSearchVH, position: Int) {
        val term = searches[position]
        holder.binding.tvRecentSearchText.text = term
        holder.binding.btnRemoveSearch.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_ID.toInt()) {
                onRemove(searches[pos])
                searches.removeAt(pos)
                notifyItemRemoved(pos)
            }
        }
    }

    fun clearAll() {
        val count = searches.size
        searches.clear()
        notifyItemRangeRemoved(0, count)
    }
}

package com.example.foodplanner.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodplanner.databinding.ItemRecentSearchBinding

class RecentSearchAdapter(
    private val searches: MutableList<String>,
    private val onItemClick: (String) -> Unit,
    private val onRemove: (String) -> Unit
) : RecyclerView.Adapter<RecentSearchAdapter.RecentSearchVH>() {

    inner class RecentSearchVH(
        val binding: ItemRecentSearchBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return searches.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentSearchVH {

        val binding = ItemRecentSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return RecentSearchVH(binding)
    }

    override fun onBindViewHolder(
        holder: RecentSearchVH,
        position: Int
    ) {

        val term = searches[position]

        holder.binding.tvRecentSearchText.text = term

        holder.binding.root.setOnClickListener {
            onItemClick(term)
        }

        holder.binding.btnRemoveSearch.setOnClickListener {

            val pos = holder.bindingAdapterPosition

            if (pos != RecyclerView.NO_POSITION) {

                val removed = searches[pos]

                searches.removeAt(pos)

                notifyItemRemoved(pos)

                onRemove(removed)
            }
        }
    }

    fun updateData(
        newSearches: List<String>
    ) {

        searches.clear()
        searches.addAll(newSearches)

        notifyDataSetChanged()
    }

    fun clearAll() {

        val count = searches.size

        searches.clear()

        notifyItemRangeRemoved(
            0,
            count
        )
    }
}
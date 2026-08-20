package com.example.foodplanner.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodplanner.R
import com.example.foodplanner.databinding.ItemCategoryGridBinding

/**
 * CategoriesGridAdapter — Engineer 1 (Lead UI/UX & Design Specialist)
 *
 * Extends RecyclerView.Adapter and overrides the 3 required methods:
 *   - getItemCount()        → course p331
 *   - onCreateViewHolder()  → course p332
 *   - onBindViewHolder()    → course p333
 *
 * Displays a 2-column grid of food categories with image, name, and meal count.
 * Uses ViewBinding for null-safe view access (course p395-402).
 */
class CategoriesGridAdapter(
    private var categories: List<CategoryGridItem>,
    private val onCategoryClick: (CategoryGridItem) -> Unit
) : RecyclerView.Adapter<CategoriesGridAdapter.CategoryGridViewHolder>() {

    /**
     * ViewHolder — holds references to views within each grid item.
     * Course ref: ViewHolder pattern p331
     */
    inner class CategoryGridViewHolder(
        private val binding: ItemCategoryGridBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CategoryGridItem) {
            binding.tvCategoryGridName.text = item.name
            binding.tvCategoryGridCount.text = item.mealCount

            // Load image with Glide — placeholder while loading (course p500-502)
            if (item.imageUrl.isNotEmpty()) {
                Glide.with(binding.root.context)
                    .load(item.imageUrl)
                    .placeholder(R.drawable.ic_chef_logo)
                    .centerCrop()
                    .into(binding.ivCategoryGridImage)
            }

            // Click listener — navigate to meals filtered by this category
            binding.root.setOnClickListener {
                onCategoryClick(item)
            }
        }
    }

    /** Returns total item count — course p331 */
    override fun getItemCount(): Int = categories.size

    /** Inflate the item layout and wrap in ViewHolder — course p332 */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryGridViewHolder {
        val binding = ItemCategoryGridBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CategoryGridViewHolder(binding)
    }

    /** Bind data to the ViewHolder at the given position — course p333 */
    override fun onBindViewHolder(holder: CategoryGridViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    /** Allows Engineer 2 to push live API data into this adapter */
    fun updateData(newCategories: List<CategoryGridItem>) {
        categories = newCategories
        notifyDataSetChanged()
    }
}

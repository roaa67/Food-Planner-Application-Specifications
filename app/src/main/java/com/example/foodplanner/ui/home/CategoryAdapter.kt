package com.example.foodplanner.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodplanner.R

/**
 * CategoryAdapter – RecyclerView Adapter for food categories.
 *
 * Course reference (p330, p335):
 *   "A custom Adapter extends from RecyclerView.Adapter and overrides these three functions:
 *    - getItemCount
 *    - onCreateViewHolder
 *    - onBindViewHolder"
 *
 * Glide used for image loading (course p633-637)
 */
class CategoryAdapter(
    private val categories: List<CategoryItem>,
    private val onItemClick: (CategoryItem) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    /**
     * ViewHolder – holds references to views in a single item row (course p335)
     * "class MyViewHolder(val row: View) : RecyclerView.ViewHolder(row)"
     */
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCategory: ImageView = itemView.findViewById(R.id.iv_category)
        val tvCategoryName: TextView = itemView.findViewById(R.id.tv_category_name)
    }

    /**
     * onCreateViewHolder – Inflate the item layout (course p335)
     * "val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)"
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    /**
     * onBindViewHolder – Bind data to view (course p335)
     * "override fun onBindViewHolder(holder: MyViewHolder, position: Int)"
     */
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]

        // Set text
        holder.tvCategoryName.text = category.name

        // Load image using Glide (course p634-637)
        // "Glide.with(context).load(url).into(imageView)"
        Glide.with(holder.itemView.context)
            .load(category.imageUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_gallery)
            .centerCrop()
            .into(holder.ivCategory)

        // Set click listener (course p234-237 - Event Handling)
        holder.itemView.setOnClickListener {
            onItemClick(category)
        }
    }

    /**
     * getItemCount – Return number of items (course p335)
     * "override fun getItemCount(): Int = data.size"
     */
    override fun getItemCount(): Int = categories.size
}

package com.example.foodplanner.ui.home

/**
 * CategoryItem – Data model for a single category row in RecyclerView.
 * Course reference (p332): "Define a model class to use as the data source"
 */
data class CategoryItem(
    val name: String,
    val imageUrl: String
)

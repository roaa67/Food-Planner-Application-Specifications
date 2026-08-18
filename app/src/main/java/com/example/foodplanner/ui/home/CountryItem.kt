package com.example.foodplanner.ui.home

/**
 * CountryItem – Data model for a single country row in RecyclerView.
 * Course reference (p332): "Define a model class to use as the data source"
 */
data class CountryItem(
    val name: String,
    val flag: String   // emoji flag e.g. "🇮🇹"
)

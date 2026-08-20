package com.example.foodplanner.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodplanner.R

/**
 * CountryAdapter – RecyclerView Adapter for countries list.
 *
 * Course reference (p330, p335):
 *   Extends RecyclerView.Adapter, overrides:
 *   - getItemCount
 *   - onCreateViewHolder
 *   - onBindViewHolder
 */
class CountryAdapter(
    private val countries: List<CountryItem>,
    private val onItemClick: (CountryItem) -> Unit
) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    /**
     * ViewHolder (course p335)
     */
    inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFlag: TextView = itemView.findViewById(R.id.tv_country_flag)
        val tvName: TextView = itemView.findViewById(R.id.tv_country_name)
    }

    /**
     * onCreateViewHolder – inflate item_country.xml (course p335)
     * "val layout = LayoutInflater.from(parent.context).inflate(..., parent, false)"
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_country, parent, false)
        return CountryViewHolder(view)
    }

    /**
     * onBindViewHolder – bind data to views (course p335)
     */
    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countries[position]

        holder.tvFlag.text = country.flag
        holder.tvName.text = country.name

        // Click listener (course p234-237 - Event Handling)
        holder.itemView.setOnClickListener {
            onItemClick(country)
        }
    }

    /**
     * getItemCount (course p335)
     */
    override fun getItemCount(): Int = countries.size
}

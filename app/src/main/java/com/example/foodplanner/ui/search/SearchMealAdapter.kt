package com.example.foodplanner.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodplanner.Meal
import com.example.foodplanner.R
import com.example.foodplanner.databinding.ItemFavoriteBinding

class SearchMealAdapter(
    private var meals: List<Meal>,
    private val onMealClick: (Meal) -> Unit
) : RecyclerView.Adapter<SearchMealAdapter.SearchMealViewHolder>() {

    inner class SearchMealViewHolder(
        private val binding: ItemFavoriteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(meal: Meal) {

            binding.tvFavMealName.text = meal.strMeal.orEmpty()
            binding.tvFavMealTime.text = "View Recipe"

            binding.btnFavHeart.visibility = View.GONE

            Glide.with(binding.root.context)
                .load(meal.strMealThumb)
                .placeholder(R.drawable.ic_chef_logo)
                .centerCrop()
                .into(binding.ivFavMealImage)

            binding.root.setOnClickListener {
                onMealClick(meal)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchMealViewHolder {

        val binding = ItemFavoriteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return SearchMealViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SearchMealViewHolder,
        position: Int
    ) {
        holder.bind(meals[position])
    }

    override fun getItemCount(): Int {
        return meals.size
    }

    fun updateData(newMeals: List<Meal>) {
        meals = newMeals
        notifyDataSetChanged()
    }
}
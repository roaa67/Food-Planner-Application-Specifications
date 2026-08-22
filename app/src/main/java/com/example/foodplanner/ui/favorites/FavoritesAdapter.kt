package com.example.foodplanner.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodplanner.R
import com.example.foodplanner.databinding.ItemFavoriteBinding

data class FavoriteMealItem(
    val name: String,
    val time: String,
    val imageUrl: String
)

class FavoritesAdapter(
    private val meals: MutableList<FavoriteMealItem>,
    private val onRemove: (FavoriteMealItem) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteVH>() {

    inner class FavoriteVH(
        val binding: ItemFavoriteBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return meals.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteVH {

        val binding =
            ItemFavoriteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return FavoriteVH(binding)
    }

    override fun onBindViewHolder(
        holder: FavoriteVH,
        position: Int
    ) {

        val meal = meals[position]

        holder.binding.tvFavMealName.text =
            meal.name

        holder.binding.tvFavMealTime.text =
            meal.time

        Glide.with(holder.binding.root.context)
            .load(meal.imageUrl)
            .placeholder(R.drawable.ic_chef_logo)
            .centerCrop()
            .into(holder.binding.ivFavMealImage)

        holder.binding.btnFavHeart.setOnClickListener {

            val pos =
                holder.bindingAdapterPosition

            if (pos != RecyclerView.NO_POSITION) {

                val removed =
                    meals[pos]

                meals.removeAt(pos)

                notifyItemRemoved(pos)

                onRemove(removed)
            }
        }
    }

    fun updateData(
        newMeals: List<FavoriteMealItem>
    ) {

        meals.clear()
        meals.addAll(newMeals)
        notifyDataSetChanged()
    }
}
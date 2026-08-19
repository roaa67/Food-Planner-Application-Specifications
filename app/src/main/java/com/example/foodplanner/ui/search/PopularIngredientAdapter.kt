package com.example.foodplanner.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodplanner.databinding.ItemSearchIngredientBinding

/**
 * Popular Ingredient Adapter — Engineer 1
 * Horizontal list of ingredient chips with circular image + name
 * Course ref: RecyclerView.Adapter p330-335
 */
class PopularIngredientAdapter(
    private var ingredients: List<String>
) : RecyclerView.Adapter<PopularIngredientAdapter.IngredientVH>() {

    inner class IngredientVH(val binding: ItemSearchIngredientBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount() = ingredients.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        IngredientVH(
            ItemSearchIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: IngredientVH, position: Int) {
        holder.binding.tvIngredientName.text = ingredients[position]
        // Engineer 2 will load actual ingredient thumbnail via Glide + MealDB /images/ingredients/{name}.png
    }
}

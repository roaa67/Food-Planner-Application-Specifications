package com.example.foodplanner.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodplanner.R
import com.example.foodplanner.databinding.ItemSearchIngredientBinding

class PopularIngredientAdapter(
    private var ingredients: List<String>,
    private val onIngredientClick: (String) -> Unit
) : RecyclerView.Adapter<PopularIngredientAdapter.IngredientVH>() {

    inner class IngredientVH(
        val binding: ItemSearchIngredientBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return ingredients.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IngredientVH {

        val binding = ItemSearchIngredientBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return IngredientVH(binding)
    }

    override fun onBindViewHolder(
        holder: IngredientVH,
        position: Int
    ) {

        val ingredient = ingredients[position]

        // Display friendly name
        val displayName =
            if (ingredient == "Potatoes") {
                "Potato"
            } else {
                ingredient
            }

        holder.binding.tvIngredientName.text = displayName

        // TheMealDB ingredient image
        val imageUrl =
            "https://www.themealdb.com/images/ingredients/${ingredient}.png"

        Glide.with(holder.binding.root.context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_chef_logo)
            .error(R.drawable.ic_chef_logo)
            .centerCrop()
            .into(holder.binding.ivIngredient)

        // Search using the real API ingredient name
        holder.binding.root.setOnClickListener {
            onIngredientClick(ingredient)
        }
    }
}
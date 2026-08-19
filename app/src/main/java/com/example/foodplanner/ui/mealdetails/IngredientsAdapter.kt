package com.example.foodplanner.ui.mealdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodplanner.R
import com.example.foodplanner.databinding.ItemIngredientBinding

data class IngredientItem(
    val name: String,
    val measure: String,
    val imageUrl: String = ""
)

/**
 * IngredientsAdapter — Engineer 1
 * Extends RecyclerView.Adapter overriding 3 required methods (course p330-335)
 */
class IngredientsAdapter(
    private var ingredients: List<IngredientItem>
) : RecyclerView.Adapter<IngredientsAdapter.IngredientVH>() {

    inner class IngredientVH(val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = ingredients.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientVH =
        IngredientVH(
            ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: IngredientVH, position: Int) {
        val item = ingredients[position]
        holder.binding.tvIngredientDetailName.text = item.name
        holder.binding.tvIngredientMeasure.text = item.measure

        val url = if (item.imageUrl.isNotEmpty()) item.imageUrl else "https://www.themealdb.com/images/ingredients/${item.name}.png"
        Glide.with(holder.binding.root.context)
            .load(url)
            .placeholder(R.drawable.ic_chef_logo)
            .into(holder.binding.ivIngredientDetail)
    }

    fun updateData(newIngredients: List<IngredientItem>) {
        ingredients = newIngredients
        notifyDataSetChanged()
    }
}

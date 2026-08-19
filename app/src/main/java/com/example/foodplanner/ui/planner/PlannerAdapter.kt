package com.example.foodplanner.ui.planner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodplanner.R
import com.example.foodplanner.databinding.ItemPlannerDayBinding

data class DayItem(
    val dayName: String,
    val mealName: String? = null,
    val mealImageUrl: String? = null
)

/**
 * PlannerAdapter — Engineer 1 (Lead UI/UX)
 * Overrides 3 required methods (course p330-335)
 * Displays 7 day rows (Mon-Sun) matching design mockup media_1787127362856.png:
 *   - Left day badge box (Mon, Tue...)
 *   - Right meal card with meal title + thumbnail image OR empty slot ("Add Meal" + plus icon)
 */
class PlannerAdapter(
    private var days: List<DayItem>,
    private val onDayClick: (DayItem) -> Unit
) : RecyclerView.Adapter<PlannerAdapter.DayVH>() {

    inner class DayVH(val binding: ItemPlannerDayBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = days.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayVH =
        DayVH(ItemPlannerDayBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: DayVH, position: Int) {
        val day = days[position]
        holder.binding.tvDayName.text = day.dayName

        if (!day.mealName.isNullOrEmpty()) {
            // Planned meal slot: show meal name and thumbnail image on right
            holder.binding.tvPlannerMealName.text = day.mealName
            holder.binding.tvPlannerMealName.visibility = View.VISIBLE
            holder.binding.ivPlannerMealImage.visibility = View.VISIBLE
            holder.binding.layoutPlannerEmpty.visibility = View.GONE

            Glide.with(holder.binding.root.context)
                .load(day.mealImageUrl)
                .placeholder(R.drawable.ic_chef_logo)
                .centerCrop()
                .into(holder.binding.ivPlannerMealImage)
        } else {
            // Empty slot: show "Add Meal" + green plus icon
            holder.binding.tvPlannerMealName.visibility = View.GONE
            holder.binding.ivPlannerMealImage.visibility = View.GONE
            holder.binding.layoutPlannerEmpty.visibility = View.VISIBLE
        }

        holder.binding.cardPlannerMeal.setOnClickListener {
            onDayClick(day)
        }
    }

    fun updateData(newDays: List<DayItem>) {
        days = newDays
        notifyDataSetChanged()
    }
}

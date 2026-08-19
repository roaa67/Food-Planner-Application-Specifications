package com.example.foodplanner.ui.planner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodplanner.R
import com.example.foodplanner.databinding.ItemPlannerDayBinding

data class DayItem(val dayName: String, val mealName: String?, val mealImageUrl: String?)

/**
 * PlannerAdapter — Engineer 1 (Lead UI/UX)
 * Shows Mon–Sun rows. Each row shows planned meal thumbnail + name, or "+ Add Meal" slot.
 * Course ref: RecyclerView.Adapter p330-335, visibility toggling p232
 */
class PlannerAdapter(
    private var days: List<DayItem>,
    private val onAddMeal: (DayItem) -> Unit
) : RecyclerView.Adapter<PlannerAdapter.DayVH>() {

    inner class DayVH(val binding: ItemPlannerDayBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount() = days.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DayVH(ItemPlannerDayBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: DayVH, position: Int) {
        val day = days[position]
        holder.binding.tvDayName.text = day.dayName

        if (day.mealName != null) {
            // Has a planned meal — show name, thumbnail, delete button (Engineer 2 sets delete action)
            holder.binding.tvPlannerMealName.text = day.mealName
            holder.binding.tvPlannerMealName.visibility = View.VISIBLE
            holder.binding.tvPlannerEmpty.visibility = View.GONE
            holder.binding.ivPlannerMealImage.visibility = View.VISIBLE
            holder.binding.btnPlannerAction.setImageResource(android.R.drawable.ic_delete)

            Glide.with(holder.binding.root.context)
                .load(day.mealImageUrl)
                .placeholder(R.drawable.ic_chef_logo)
                .into(holder.binding.ivPlannerMealImage)
        } else {
            // Empty slot — show "Tap + to plan a meal" and + button
            holder.binding.tvPlannerMealName.visibility = View.GONE
            holder.binding.tvPlannerEmpty.visibility = View.VISIBLE
            holder.binding.ivPlannerMealImage.visibility = View.GONE
            holder.binding.btnPlannerAction.setImageResource(android.R.drawable.ic_input_add)
        }

        holder.binding.btnPlannerAction.setOnClickListener {
            onAddMeal(day)
        }
    }

    fun updateData(newDays: List<DayItem>) {
        days = newDays
        notifyDataSetChanged()
    }
}

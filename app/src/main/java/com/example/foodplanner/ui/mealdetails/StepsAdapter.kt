package com.example.foodplanner.ui.mealdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodplanner.databinding.ItemStepBinding

data class StepItem(
    val number: Int,
    val text: String
)

/**
 * StepsAdapter — Engineer 1
 * Overrides 3 required methods (course p330-335)
 * Shows green circle number badge + step description text matching mockup media_1787124418496.png
 */
class StepsAdapter(
    private var steps: List<StepItem>
) : RecyclerView.Adapter<StepsAdapter.StepVH>() {

    inner class StepVH(val binding: ItemStepBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = steps.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepVH =
        StepVH(
            ItemStepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: StepVH, position: Int) {
        val item = steps[position]
        holder.binding.tvStepNumber.text = item.number.toString()
        holder.binding.tvStepText.text = item.text
    }

    fun updateData(newSteps: List<StepItem>) {
        steps = newSteps
        notifyDataSetChanged()
    }
}

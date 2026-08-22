package com.example.foodplanner.ui.mealdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodplanner.databinding.ItemStepBinding

data class StepItem(
    val number: Int,
    val text: String
)

class StepsAdapter(
    private var steps: List<StepItem>
) : RecyclerView.Adapter<StepsAdapter.StepVH>() {

    inner class StepVH(
        val binding: ItemStepBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return steps.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StepVH {

        val binding =
            ItemStepBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return StepVH(binding)
    }

    override fun onBindViewHolder(
        holder: StepVH,
        position: Int
    ) {

        val item = steps[position]

        holder.binding.tvStepNumber.text =
            item.number.toString()

        holder.binding.tvStepText.apply {

            text = item.text

            maxLines = Integer.MAX_VALUE
            isSingleLine = false

            // Make sure RecyclerView recalculates the item height
            post {
                requestLayout()
            }
        }

        holder.binding.root.post {
            holder.binding.root.requestLayout()
        }
    }

    fun updateData(
        newSteps: List<StepItem>
    ) {

        steps = newSteps

        notifyDataSetChanged()
    }
}
package com.example.foodplanner.ui.mealdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodplanner.databinding.FragmentMealStepsBinding

/**
 * StepsFragment — Engineer 1
 * Tab 2 of MealDetailsActivity — lists step-by-step cooking instructions with green numbered circle badges
 * Course ref: Fragment p380, RecyclerView p330-335
 */
class StepsFragment : Fragment() {
    private var _binding: FragmentMealStepsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: StepsAdapter

    companion object {
        fun newInstance(mealId: String) = StepsFragment().apply {
            arguments = Bundle().also { it.putString("MEAL_ID", mealId) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMealStepsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val defaultSteps = listOf(
            StepItem(1, "Marinate the chicken with lemon juice, garlic, salt and pepper."),
            StepItem(2, "Heat olive oil in a pan."),
            StepItem(3, "Cook the chicken for 6-7 minutes on each side."),
            StepItem(4, "Let it rest for a few minutes."),
            StepItem(5, "Serve with your favorite sides.")
        )

        adapter = StepsAdapter(defaultSteps)
        binding.rvSteps.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSteps.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

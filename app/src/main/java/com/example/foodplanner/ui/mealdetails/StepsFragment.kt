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
 * Tab 2 of MealDetailsActivity — numbered cooking steps
 * Course ref: Fragment p380, RecyclerView p330
 */
class StepsFragment : Fragment() {
    private var _binding: FragmentMealStepsBinding? = null
    private val binding get() = _binding!!

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
        binding.rvSteps.layoutManager = LinearLayoutManager(requireContext())
        // TODO: Engineer 2 parses strInstructions from Meal object and attaches StepsAdapter
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}

package com.example.foodplanner.ui.mealdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodplanner.databinding.FragmentMealIngredientsBinding

/**
 * IngredientsFragment — Engineer 1
 * Tab 1 of MealDetailsActivity — lists all ingredients with image + measure
 * Course ref: Fragment p380, RecyclerView p330
 */
class IngredientsFragment : Fragment() {
    private var _binding: FragmentMealIngredientsBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(mealId: String) = IngredientsFragment().apply {
            arguments = Bundle().also { it.putString("MEAL_ID", mealId) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMealIngredientsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvIngredients.layoutManager = LinearLayoutManager(requireContext())
        // TODO: Engineer 2 attaches IngredientsAdapter with live data from Meal object
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}

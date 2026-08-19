package com.example.foodplanner.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodplanner.databinding.FragmentCategoriesBinding

/**
 * CategoriesFragment — Engineer 1 (Lead UI/UX & Design Specialist)
 *
 * Displays a 2-column grid of all food categories.
 * Course ref: Fragment lifecycle p380-382, RecyclerView with GridLayoutManager p335
 */
class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoriesGridAdapter: CategoriesGridAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadPlaceholderData()
    }

    /**
     * Set up 2-column GridLayoutManager — course p335 (GridLayoutManager)
     */
    private fun setupRecyclerView() {
        categoriesGridAdapter = CategoriesGridAdapter(emptyList()) { category ->
            // TODO: Navigate to filtered meal list (Engineer 2 — Presenter/API)
        }
        binding.rvCategoriesGrid.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = categoriesGridAdapter
        }
    }

    /**
     * Placeholder data for UI preview — real data injected by Engineer 2 via ViewModel
     */
    private fun loadPlaceholderData() {
        val placeholders = listOf(
            CategoryGridItem("Breakfast", "45 meals", ""),
            CategoryGridItem("Main Course", "120 meals", ""),
            CategoryGridItem("Desserts", "60 meals", ""),
            CategoryGridItem("Salads", "30 meals", ""),
            CategoryGridItem("Soup", "28 meals", ""),
            CategoryGridItem("Drinks", "34 meals", "")
        )
        categoriesGridAdapter.updateData(placeholders)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks (course p382)
    }
}

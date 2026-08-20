package com.example.foodplanner.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodplanner.databinding.FragmentSearchBinding

/**
 * SearchFragment — Engineer 1 (Lead UI/UX & Design Specialist)
 *
 * Implements:
 *  - Search text input with real-time listener (TextWatcher)
 *  - Filter chips: By Ingredient / By Country / By Category
 *  - Popular Ingredients horizontal list
 *  - Recent Searches list with remove/clear-all
 *  - Search results list (shown after typing)
 *
 * Course ref: Fragment p380, RecyclerView p330-335, Material Chips p549
 */
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var ingredientsAdapter: PopularIngredientAdapter
    private lateinit var recentSearchAdapter: RecentSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearchInput()
        setupPopularIngredients()
        setupRecentSearches()
        setupChipFilters()
        setupClearAll()
    }

    /**
     * TextWatcher on search field — toggles results vs recent view
     * Course ref: TextInputLayout p555, EditText listeners
     */
    private fun setupSearchInput() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val hasText = !s.isNullOrBlank()
                binding.rvSearchResults.visibility = if (hasText) View.VISIBLE else View.GONE
                // TODO: Engineer 2 calls API with s.toString() and updates rv_search_results
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    /**
     * Chip selection listener — switches search mode
     * Course ref: Material Chips p549
     */
    private fun setupChipFilters() {
        binding.chipGroupFilter.setOnCheckedStateChangeListener { _, checkedIds ->
            // TODO: Engineer 2 — pass filter type to presenter/repo
        }
    }

    /**
     * Popular Ingredients horizontal RecyclerView
     */
    private fun setupPopularIngredients() {
        val ingredients = listOf("Chicken", "Tomato", "Potato", "Rice", "Cheese", "Garlic")
        ingredientsAdapter = PopularIngredientAdapter(ingredients)
        binding.rvPopularIngredients.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = ingredientsAdapter
        }
    }

    /**
     * Recent Searches vertical list with swipe-to-delete or X button
     */
    private fun setupRecentSearches() {
        val recent = mutableListOf("Pasta", "Chicken Curry", "Beef Steak")
        recentSearchAdapter = RecentSearchAdapter(recent) { removedItem ->
            // SharedPreferences remove — Engineer 1 handles local UI state only
        }
        binding.rvRecentSearches.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recentSearchAdapter
        }
    }

    private fun setupClearAll() {
        binding.tvClearAll.setOnClickListener {
            recentSearchAdapter.clearAll()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

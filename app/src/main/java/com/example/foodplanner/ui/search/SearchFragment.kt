package com.example.foodplanner.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodplanner.Meal
import com.example.foodplanner.RetrofitClient
import com.example.foodplanner.databinding.FragmentSearchBinding
import com.example.foodplanner.ui.mealdetails.MealDetailsActivity
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONArray

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var ingredientsAdapter: PopularIngredientAdapter
    private lateinit var recentSearchAdapter: RecentSearchAdapter
    private lateinit var searchMealAdapter: SearchMealAdapter

    private val disposables = CompositeDisposable()

    private var currentSearchMode = "INGREDIENT"
    private var ignoreTextChange = false

    companion object {

        private const val ARG_SEARCH_MODE = "SEARCH_MODE"
        private const val ARG_SEARCH_VALUE = "SEARCH_VALUE"

        private const val PREFS_NAME = "food_planner_search_prefs"
        private const val KEY_RECENT_SEARCHES = "recent_searches"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchResults()
        setupRecentSearches()
        setupSearchInput()
        setupPopularIngredients()
        setupChipFilters()
        setupClearAll()
        handleArguments()
    }

    private fun setupSearchResults() {

        searchMealAdapter =
            SearchMealAdapter(
                emptyList()
            ) { meal ->

                openMealDetails(meal)
            }

        binding.rvSearchResults.apply {

            layoutManager =
                LinearLayoutManager(
                    requireContext()
                )

            adapter = searchMealAdapter
        }
    }

    private fun handleArguments() {

        val searchMode =
            arguments
                ?.getString(ARG_SEARCH_MODE)
                .orEmpty()

        val searchValue =
            arguments
                ?.getString(ARG_SEARCH_VALUE)
                .orEmpty()

        if (searchValue.isBlank()) {
            return
        }

        ignoreTextChange = true

        when (searchMode) {

            "CATEGORY" -> {

                currentSearchMode = "CATEGORY"

                binding.chipByCategory.isChecked = true
            }

            "COUNTRY" -> {

                currentSearchMode = "COUNTRY"

                binding.chipByCountry.isChecked = true
            }

            "INGREDIENT" -> {

                currentSearchMode = "INGREDIENT"

                binding.chipByIngredient.isChecked = true
            }

            else -> {

                currentSearchMode = "INGREDIENT"

                binding.chipByIngredient.isChecked = true
            }
        }

        binding.etSearch.setText(searchValue)

        binding.etSearch.setSelection(
            searchValue.length
        )

        ignoreTextChange = false

        binding.rvSearchResults.visibility =
            View.VISIBLE

        saveRecentSearch(searchValue)

        performSearch(searchValue)
    }

    private fun setupSearchInput() {

        binding.etSearch.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                    if (ignoreTextChange) {
                        return
                    }

                    val query =
                        s?.toString()
                            ?.trim()
                            .orEmpty()

                    if (query.isBlank()) {

                        binding.rvSearchResults.visibility =
                            View.GONE

                        searchMealAdapter.updateData(
                            emptyList()
                        )

                        return
                    }

                    binding.rvSearchResults.visibility =
                        View.VISIBLE

                    performSearch(query)
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                val query =
                    binding.etSearch.text
                        ?.toString()
                        ?.trim()
                        .orEmpty()

                if (query.isNotBlank()) {

                    saveRecentSearch(query)

                    performSearch(query)
                }

                true

            } else {

                false
            }
        }
    }

    private fun setupChipFilters() {

        binding.chipGroupFilter
            .setOnCheckedStateChangeListener { _, checkedIds ->

                currentSearchMode =
                    when {

                        checkedIds.contains(
                            binding.chipByCategory.id
                        ) -> {
                            "CATEGORY"
                        }

                        checkedIds.contains(
                            binding.chipByCountry.id
                        ) -> {
                            "COUNTRY"
                        }

                        else -> {
                            "INGREDIENT"
                        }
                    }

                val query =
                    binding.etSearch.text
                        ?.toString()
                        ?.trim()
                        .orEmpty()

                if (
                    query.isNotBlank() &&
                    !ignoreTextChange
                ) {

                    performSearch(query)
                }
            }
    }

    private fun performSearch(
        query: String
    ) {

        when (currentSearchMode) {

            "CATEGORY" -> {
                searchByCategory(query)
            }

            "COUNTRY" -> {
                searchByCountry(query)
            }

            else -> {
                searchByIngredient(query)
            }
        }
    }

    private fun searchByCategory(
        category: String
    ) {

        val disposable =
            RetrofitClient.apiService
                .getMealsByCategory(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->

                        showSearchResults(
                            response.meals.orEmpty()
                        )
                    },
                    { error ->

                        showSearchError(
                            "Category search failed",
                            error
                        )
                    }
                )

        disposables.add(disposable)
    }

    private fun searchByCountry(
        country: String
    ) {

        val disposable =
            RetrofitClient.apiService
                .getMealsByArea(country)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->

                        showSearchResults(
                            response.meals.orEmpty()
                        )
                    },
                    { error ->

                        showSearchError(
                            "Country search failed",
                            error
                        )
                    }
                )

        disposables.add(disposable)
    }

    private fun searchByIngredient(
        ingredient: String
    ) {

        val apiIngredient =
            when (
                ingredient
                    .trim()
                    .lowercase()
            ) {

                "potato" -> {
                    "Potatoes"
                }

                else -> {
                    ingredient.trim()
                }
            }

        val disposable =
            RetrofitClient.apiService
                .getMealsByIngredient(apiIngredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->

                        showSearchResults(
                            response.meals.orEmpty()
                        )
                    },
                    { error ->

                        showSearchError(
                            "Ingredient search failed",
                            error
                        )
                    }
                )

        disposables.add(disposable)
    }

    private fun showSearchResults(
        meals: List<Meal>
    ) {

        binding.rvSearchResults.visibility =
            View.VISIBLE

        searchMealAdapter.updateData(meals)

        if (meals.isEmpty()) {

            Snackbar.make(
                binding.root,
                "No meals found for this selection",
                Snackbar.LENGTH_LONG
            ).show()
        }

        Log.d(
            "SearchFragment",
            "Results: ${meals.size}"
        )
    }

    private fun showSearchError(
        message: String,
        error: Throwable
    ) {

        Log.e(
            "SearchFragment",
            message,
            error
        )

        if (isAdded) {

            Snackbar.make(
                binding.root,
                message,
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun openMealDetails(
        meal: Meal
    ) {

        val mealId =
            meal.idMeal.orEmpty()

        if (mealId.isBlank()) {
            return
        }

        val disposable =
            RetrofitClient.apiService
                .getMealDetails(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->

                        val fullMeal =
                            response.meals
                                ?.firstOrNull()

                        if (fullMeal != null) {

                            val intent =
                                Intent(
                                    requireContext(),
                                    MealDetailsActivity::class.java
                                ).apply {

                                    putExtra(
                                        "MEAL_ID",
                                        fullMeal.idMeal.orEmpty()
                                    )

                                    putExtra(
                                        "MEAL_NAME",
                                        fullMeal.strMeal.orEmpty()
                                    )

                                    putExtra(
                                        "MEAL_IMAGE",
                                        fullMeal.strMealThumb.orEmpty()
                                    )

                                    putExtra(
                                        "MEAL_AREA",
                                        fullMeal.strArea.orEmpty()
                                    )

                                    putExtra(
                                        "MEAL_YOUTUBE",
                                        fullMeal.strYoutube.orEmpty()
                                    )
                                }

                            startActivity(intent)
                        }
                    },
                    { error ->

                        showSearchError(
                            "Failed to load meal details",
                            error
                        )
                    }
                )

        disposables.add(disposable)
    }

    private fun setupPopularIngredients() {

        val ingredients =
            listOf(
                "Chicken",
                "Tomato",
                "Potatoes",
                "Rice",
                "Cheese",
                "Garlic"
            )

        ingredientsAdapter =
            PopularIngredientAdapter(
                ingredients
            ) { ingredient ->

                currentSearchMode =
                    "INGREDIENT"

                ignoreTextChange =
                    true

                binding.chipByIngredient.isChecked =
                    true

                val displayName =
                    if (ingredient == "Potatoes") {
                        "Potato"
                    } else {
                        ingredient
                    }

                binding.etSearch.setText(
                    displayName
                )

                binding.etSearch.setSelection(
                    displayName.length
                )

                ignoreTextChange =
                    false

                binding.rvSearchResults.visibility =
                    View.VISIBLE

                saveRecentSearch(
                    displayName
                )

                searchByIngredient(
                    ingredient
                )
            }

        binding.rvPopularIngredients.apply {

            layoutManager =
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )

            adapter =
                ingredientsAdapter
        }
    }

    private fun setupRecentSearches() {

        val recent =
            loadRecentSearches()
                .toMutableList()

        recentSearchAdapter =
            RecentSearchAdapter(
                recent,
                onItemClick = { term ->

                    ignoreTextChange = true

                    binding.etSearch.setText(term)

                    binding.etSearch.setSelection(
                        term.length
                    )

                    ignoreTextChange = false

                    binding.rvSearchResults.visibility =
                        View.VISIBLE

                    performSearch(term)
                },
                onRemove = { removedItem ->

                    removeRecentSearch(
                        removedItem
                    )
                }
            )

        binding.rvRecentSearches.apply {

            layoutManager =
                LinearLayoutManager(
                    requireContext()
                )

            adapter =
                recentSearchAdapter
        }
    }

    private fun saveRecentSearch(
        search: String
    ) {

        val value =
            search.trim()

        if (value.isBlank()) {
            return
        }

        val recent =
            loadRecentSearches()
                .toMutableList()

        recent.removeAll {
            it.equals(
                value,
                ignoreCase = true
            )
        }

        recent.add(
            0,
            value
        )

        if (recent.size > 10) {

            recent.subList(
                10,
                recent.size
            ).clear()
        }

        saveRecentSearchList(
            recent
        )

        if (
            ::recentSearchAdapter.isInitialized
        ) {

            recentSearchAdapter.updateData(
                recent
            )
        }
    }

    private fun removeRecentSearch(
        search: String
    ) {

        val recent =
            loadRecentSearches()
                .toMutableList()

        recent.removeAll {
            it.equals(
                search,
                ignoreCase = true
            )
        }

        saveRecentSearchList(
            recent
        )
    }

    private fun loadRecentSearches(): List<String> {

        val prefs =
            requireContext()
                .getSharedPreferences(
                    PREFS_NAME,
                    Context.MODE_PRIVATE
                )

        val json =
            prefs.getString(
                KEY_RECENT_SEARCHES,
                null
            ) ?: return emptyList()

        return try {

            val array =
                JSONArray(json)

            val result =
                mutableListOf<String>()

            for (
            index in 0
                    until array.length()
            ) {

                result.add(
                    array.getString(index)
                )
            }

            result

        } catch (
            e: Exception
        ) {

            emptyList()
        }
    }

    private fun saveRecentSearchList(
        recent: List<String>
    ) {

        val array =
            JSONArray()

        recent.forEach {
            array.put(it)
        }

        requireContext()
            .getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .putString(
                KEY_RECENT_SEARCHES,
                array.toString()
            )
            .apply()
    }

    private fun setupClearAll() {

        binding.tvClearAll
            .setOnClickListener {

                requireContext()
                    .getSharedPreferences(
                        PREFS_NAME,
                        Context.MODE_PRIVATE
                    )
                    .edit()
                    .remove(
                        KEY_RECENT_SEARCHES
                    )
                    .apply()

                recentSearchAdapter.clearAll()
            }
    }

    override fun onDestroyView() {

        disposables.clear()

        _binding = null

        super.onDestroyView()
    }
}
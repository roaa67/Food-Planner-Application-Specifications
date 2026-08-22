package com.example.foodplanner.ui.mealdetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodplanner.Meal
import com.example.foodplanner.R
import com.example.foodplanner.RetrofitClient
import com.example.foodplanner.databinding.FragmentMealIngredientsBinding
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class IngredientsFragment : Fragment() {

    private var _binding: FragmentMealIngredientsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: IngredientsAdapter

    private val disposables = CompositeDisposable()

    companion object {

        private const val ARG_MEAL_ID = "MEAL_ID"

        fun newInstance(mealId: String) =
            IngredientsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MEAL_ID, mealId)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            FragmentMealIngredientsBinding.inflate(
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

        setupRecyclerView()

        val mealId =
            arguments?.getString(ARG_MEAL_ID).orEmpty()

        if (mealId.isNotBlank()) {
            loadMealIngredients(mealId)
        }

        setupFavoriteButton()
    }

    private fun setupRecyclerView() {

        adapter =
            IngredientsAdapter(
                emptyList()
            )

        binding.rvIngredients.apply {

            layoutManager =
                LinearLayoutManager(
                    requireContext()
                )

            adapter =
                this@IngredientsFragment.adapter

            // RecyclerView handles its own scrolling
            isNestedScrollingEnabled = true

            // Ingredient cards may have varying content
            setHasFixedSize(false)

            itemAnimator = null
        }
    }

    private fun loadMealIngredients(
        mealId: String
    ) {

        val disposable =
            RetrofitClient.apiService
                .getMealDetails(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->

                        val meal =
                            response.meals
                                ?.firstOrNull()

                        if (meal != null) {

                            val ingredients =
                                buildIngredientList(meal)

                            adapter =
                                IngredientsAdapter(
                                    ingredients
                                )

                            binding.rvIngredients.adapter =
                                adapter

                            Log.d(
                                "IngredientsFragment",
                                "Loaded ${ingredients.size} ingredients"
                            )
                        }
                    },
                    { error ->

                        Log.e(
                            "IngredientsFragment",
                            "Meal details API error",
                            error
                        )

                        if (isAdded) {

                            Snackbar.make(
                                binding.root,
                                "Failed to load ingredients",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                )

        disposables.add(disposable)
    }

    private fun setupFavoriteButton() {

        binding.btnAddToFavorites
            .setOnClickListener {

                Snackbar.make(
                    binding.root,
                    getString(
                        R.string.added_to_favorites
                    ),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
    }

    private fun buildIngredientList(
        meal: Meal
    ): List<IngredientItem> {

        val ingredients =
            listOf(
                meal.strIngredient1,
                meal.strIngredient2,
                meal.strIngredient3,
                meal.strIngredient4,
                meal.strIngredient5,
                meal.strIngredient6,
                meal.strIngredient7,
                meal.strIngredient8,
                meal.strIngredient9,
                meal.strIngredient10,
                meal.strIngredient11,
                meal.strIngredient12,
                meal.strIngredient13,
                meal.strIngredient14,
                meal.strIngredient15,
                meal.strIngredient16,
                meal.strIngredient17,
                meal.strIngredient18,
                meal.strIngredient19,
                meal.strIngredient20
            )

        val measures =
            listOf(
                meal.strMeasure1,
                meal.strMeasure2,
                meal.strMeasure3,
                meal.strMeasure4,
                meal.strMeasure5,
                meal.strMeasure6,
                meal.strMeasure7,
                meal.strMeasure8,
                meal.strMeasure9,
                meal.strMeasure10,
                meal.strMeasure11,
                meal.strMeasure12,
                meal.strMeasure13,
                meal.strMeasure14,
                meal.strMeasure15,
                meal.strMeasure16,
                meal.strMeasure17,
                meal.strMeasure18,
                meal.strMeasure19,
                meal.strMeasure20
            )

        return ingredients
            .zip(measures)
            .filter { pair ->

                !pair.first
                    .isNullOrBlank()
            }
            .map { pair ->

                IngredientItem(
                    name =
                        pair.first
                            .orEmpty()
                            .trim(),

                    measure =
                        pair.second
                            .orEmpty()
                            .trim()
                )
            }
    }

    override fun onDestroyView() {

        disposables.clear()

        _binding = null

        super.onDestroyView()
    }
}
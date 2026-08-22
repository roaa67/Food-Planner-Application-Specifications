package com.example.foodplanner.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodplanner.Meal
import com.example.foodplanner.R
import com.example.foodplanner.RetrofitClient
import com.example.foodplanner.ui.categories.CategoriesFragment
import com.example.foodplanner.ui.mealdetails.MealDetailsActivity
import com.example.foodplanner.ui.search.SearchFragment
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeFragment : Fragment() {

    private lateinit var rvCategories: RecyclerView
    private lateinit var rvCountries: RecyclerView

    private lateinit var ivMealOfDay: ImageView
    private lateinit var tvMealName: TextView

    private val disposables = CompositeDisposable()

    private var currentMeal: Meal? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_home,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        rvCategories = view.findViewById(R.id.rv_categories)
        rvCountries = view.findViewById(R.id.rv_countries)

        ivMealOfDay = view.findViewById(R.id.iv_meal_of_day)
        tvMealName = view.findViewById(R.id.tv_meal_name)

        val seeAllCategories =
            view.findViewById<TextView>(R.id.tv_see_all_categories)

        // =========================================
        // See All Categories
        // =========================================

        seeAllCategories.setOnClickListener {

            parentFragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragment_container,
                    CategoriesFragment()
                )
                .addToBackStack(null)
                .commit()
        }

        setupCategoriesRecyclerView()
        setupCountriesRecyclerView()
        setupMealOfDay(view)
    }

    // =========================================
    // Categories API
    // =========================================

    private fun setupCategoriesRecyclerView() {

        rvCategories.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        val disposable =
            RetrofitClient.apiService
                .getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->

                        val categories =
                            response.categories
                                ?.map { category ->

                                    CategoryItem(
                                        name = category.strCategory.orEmpty(),
                                        imageUrl = category.strCategoryThumb.orEmpty()
                                    )
                                }
                                ?: emptyList()

                        rvCategories.adapter =
                            CategoryAdapter(categories) { category ->

                                Log.d(
                                    "HomeFragment",
                                    "Selected category: ${category.name}"
                                )

                                val searchFragment =
                                    SearchFragment().apply {

                                        arguments = Bundle().apply {

                                            putString(
                                                "SEARCH_MODE",
                                                "CATEGORY"
                                            )

                                            putString(
                                                "SEARCH_VALUE",
                                                category.name
                                            )
                                        }
                                    }

                                parentFragmentManager
                                    .beginTransaction()
                                    .replace(
                                        R.id.fragment_container,
                                        searchFragment
                                    )
                                    .addToBackStack(null)
                                    .commit()
                            }
                    },
                    { error ->

                        Log.e(
                            "HomeFragment",
                            "Categories API Error",
                            error
                        )

                        if (isAdded) {

                            Snackbar.make(
                                rvCategories,
                                "Failed to load categories",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                )

        disposables.add(disposable)
    }

    // =========================================
    // Countries API
    // =========================================

    private fun setupCountriesRecyclerView() {

        rvCountries.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        val disposable =
            RetrofitClient.apiService
                .getAreas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->

                        val countries =
                            response.meals
                                ?.map { area ->

                                    val countryName =
                                        area.strArea.orEmpty()

                                    CountryItem(
                                        name = countryName,
                                        flag = getCountryFlag(countryName)
                                    )
                                }
                                ?: emptyList()

                        rvCountries.adapter =
                            CountryAdapter(countries) { country ->

                                Log.d(
                                    "HomeFragment",
                                    "Selected country: ${country.name}"
                                )

                                val searchFragment =
                                    SearchFragment().apply {

                                        arguments = Bundle().apply {

                                            putString(
                                                "SEARCH_MODE",
                                                "COUNTRY"
                                            )

                                            putString(
                                                "SEARCH_VALUE",
                                                country.name
                                            )
                                        }
                                    }

                                parentFragmentManager
                                    .beginTransaction()
                                    .replace(
                                        R.id.fragment_container,
                                        searchFragment
                                    )
                                    .addToBackStack(null)
                                    .commit()
                            }
                    },
                    { error ->

                        Log.e(
                            "HomeFragment",
                            "Countries API Error",
                            error
                        )

                        if (isAdded) {

                            Snackbar.make(
                                rvCountries,
                                "Failed to load countries",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                )

        disposables.add(disposable)
    }

    // =========================================
    // Meal Of The Day API
    // =========================================

    private fun setupMealOfDay(view: View) {

        val btnFavorite =
            view.findViewById<View>(R.id.btn_add_favorite)

        val btnViewRecipe =
            view.findViewById<View>(R.id.btn_view_recipe)

        val disposable =
            RetrofitClient.apiService
                .getMealOfTheDay()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->

                        val meal =
                            response.meals?.firstOrNull()

                        if (meal != null) {

                            currentMeal = meal

                            Log.d(
                                "HomeFragment",
                                "YouTube URL = ${meal.strYoutube}"
                            )

                            tvMealName.text =
                                meal.strMeal.orEmpty()

                            Glide.with(this)
                                .load(meal.strMealThumb)
                                .centerCrop()
                                .into(ivMealOfDay)

                            Log.d(
                                "HomeFragment",
                                "Meal of Day: ${meal.strMeal}"
                            )
                        }
                    },
                    { error ->

                        Log.e(
                            "HomeFragment",
                            "Meal Of Day API Error",
                            error
                        )

                        if (isAdded) {

                            Snackbar.make(
                                view,
                                "Failed to load Meal of the Day",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                )

        disposables.add(disposable)

        // =====================================
        // Favorite Button
        // =====================================

        btnFavorite.setOnClickListener {

            val meal = currentMeal

            if (meal == null) {

                Snackbar.make(
                    view,
                    "Meal is still loading",
                    Snackbar.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            Snackbar.make(
                view,
                R.string.added_to_favorites,
                Snackbar.LENGTH_LONG
            )
                .setAction(
                    R.string.snackbar_undo
                ) {
                    // Room integration later
                }
                .show()

            Log.d(
                "HomeFragment",
                "Favorite meal: ${meal.strMeal}"
            )
        }

        // =====================================
        // View Recipe -> MealDetailsActivity
        // =====================================

        btnViewRecipe.setOnClickListener {

            val meal = currentMeal

            if (meal == null) {

                Snackbar.make(
                    view,
                    "Meal is still loading",
                    Snackbar.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val intent =
                Intent(
                    requireContext(),
                    MealDetailsActivity::class.java
                ).apply {

                    putExtra(
                        "MEAL_ID",
                        meal.idMeal.orEmpty()
                    )

                    putExtra(
                        "MEAL_NAME",
                        meal.strMeal.orEmpty()
                    )

                    putExtra(
                        "MEAL_IMAGE",
                        meal.strMealThumb.orEmpty()
                    )

                    putExtra(
                        "MEAL_AREA",
                        meal.strArea.orEmpty()
                    )

                    putExtra(
                        "MEAL_YOUTUBE",
                        meal.strYoutube.orEmpty()
                    )
                }

            startActivity(intent)
        }
    }

    // =========================================
    // Country Flags
    // =========================================

    private fun getCountryFlag(country: String): String {
        return when (country.trim().lowercase()) {

            "american" -> "🇺🇸"
            "british" -> "🇬🇧"
            "canadian" -> "🇨🇦"
            "chinese" -> "🇨🇳"
            "croatian" -> "🇭🇷"
            "dutch" -> "🇳🇱"
            "egyptian" -> "🇪🇬"
            "filipino" -> "🇵🇭"
            "french" -> "🇫🇷"
            "greek" -> "🇬🇷"
            "indian" -> "🇮🇳"
            "irish" -> "🇮🇪"
            "italian" -> "🇮🇹"
            "jamaican" -> "🇯🇲"
            "japanese" -> "🇯🇵"
            "kenyan" -> "🇰🇪"
            "malaysian" -> "🇲🇾"
            "mexican" -> "🇲🇽"
            "moroccan" -> "🇲🇦"
            "polish" -> "🇵🇱"
            "portuguese" -> "🇵🇹"
            "russian" -> "🇷🇺"
            "spanish" -> "🇪🇸"
            "thai" -> "🇹🇭"
            "tunisian" -> "🇹🇳"
            "turkish" -> "🇹🇷"
            "ukrainian" -> "🇺🇦"
            "uruguayan" -> "🇺🇾"
            "vietnamese" -> "🇻🇳"

            // Extra areas returned by API
            "afghan" -> "🇦🇫"
            "albanian" -> "🇦🇱"
            "algerian" -> "🇩🇿"
            "andorran" -> "🇦🇩"
            "australian" -> "🇦🇺"
            "austrian" -> "🇦🇹"
            "belgian" -> "🇧🇪"
            "brazilian" -> "🇧🇷"
            "cambodian" -> "🇰🇭"
            "cuban" -> "🇨🇺"
            "danish" -> "🇩🇰"
            "finnish" -> "🇫🇮"
            "german" -> "🇩🇪"
            "hungarian" -> "🇭🇺"
            "icelandic" -> "🇮🇸"
            "indonesian" -> "🇮🇩"
            "israeli" -> "🇮🇱"
            "lebanese" -> "🇱🇧"
            "norwegian" -> "🇳🇴"
            "saudi arabian" -> "🇸🇦"
            "serbian" -> "🇷🇸"
            "singaporean" -> "🇸🇬"
            "slovakian" -> "🇸🇰"
            "south african" -> "🇿🇦"
            "south korean", "korean" -> "🇰🇷"
            "swedish" -> "🇸🇪"
            "swiss" -> "🇨🇭"

            else -> "🌍"
        }
    }
    override fun onDestroyView() {

        disposables.clear()

        super.onDestroyView()
    }
}
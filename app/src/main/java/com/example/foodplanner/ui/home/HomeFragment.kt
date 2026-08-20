package com.example.foodplanner.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodplanner.R
import com.google.android.material.snackbar.Snackbar

/**
 * HomeFragment – Displays Meal of the Day, Categories, and Countries.
 *
 * Course reference:
 *   - Fragment (p392-435): extends Fragment, overrides onCreateView
 *   - onCreateView (p412): "inflate the layout for this fragment"
 *   - RecyclerView setup (p332-336): LayoutManager + Adapter binding
 *   - Snackbar (p560-561): feedback when adding to favorites
 */
class HomeFragment : Fragment() {

    private lateinit var rvCategories: RecyclerView
    private lateinit var rvCountries: RecyclerView

    /**
     * onCreateView – Inflate the fragment's layout (course p412-413)
     * "Inflate the layout for this Fragment"
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate using LayoutInflater (course p318 - Layout Inflater)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    /**
     * onViewCreated – Called after the view is created (course p413)
     * Set up RecyclerViews and click listeners here
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find RecyclerViews (course p140 - findViewById)
        rvCategories = view.findViewById(R.id.rv_categories)
        rvCountries = view.findViewById(R.id.rv_countries)

        setupCategoriesRecyclerView()
        setupCountriesRecyclerView()
        setupMealOfDay(view)
    }

    /**
     * Setup Categories RecyclerView (course p332-336)
     * Steps:
     * 1. Create data source (dummy data for now)
     * 2. Set LayoutManager (horizontal LinearLayoutManager)
     * 3. Create and set adapter
     */
    private fun setupCategoriesRecyclerView() {
        // Dummy data – will be replaced by API call (TheMealDB)
        val dummyCategories = listOf(
            CategoryItem("Breakfast", "https://www.themealdb.com/images/category/breakfast.png"),
            CategoryItem("Main Course", "https://www.themealdb.com/images/category/maincourse.png"),
            CategoryItem("Desserts", "https://www.themealdb.com/images/category/desserts.png"),
            CategoryItem("Salads", "https://www.themealdb.com/images/category/salads.png"),
            CategoryItem("Soup", "https://www.themealdb.com/images/category/soup.png"),
            CategoryItem("Drinks", "https://www.themealdb.com/images/category/drinks.png")
        )

        // Set LayoutManager – horizontal scroll (course p336: rv.layoutManager = LinearLayoutManager)
        rvCategories.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        // Set adapter (course p336: rv.adapter = MyAdapter(...))
        rvCategories.adapter = CategoryAdapter(dummyCategories) { category ->
            // Item click – navigate to category meals (TODO)
        }
    }

    /**
     * Setup Countries RecyclerView (course p332-336)
     */
    private fun setupCountriesRecyclerView() {
        // Dummy data – will be replaced by API call (TheMealDB list.php?a=list)
        val dummyCountries = listOf(
            CountryItem("Italian", "🇮🇹"),
            CountryItem("Mexican", "🇲🇽"),
            CountryItem("Japanese", "🇯🇵"),
            CountryItem("Indian", "🇮🇳"),
            CountryItem("American", "🇺🇸"),
            CountryItem("British", "🇬🇧"),
            CountryItem("French", "🇫🇷"),
            CountryItem("Chinese", "🇨🇳")
        )

        rvCountries.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        rvCountries.adapter = CountryAdapter(dummyCountries) { country ->
            // Item click – navigate to country meals (TODO)
        }
    }

    /**
     * Setup Meal of the Day card interactions
     * Snackbar on favorite click (course p560-561)
     */
    private fun setupMealOfDay(view: View) {
        val btnFavorite = view.findViewById<View>(R.id.btn_add_favorite)
        val btnViewRecipe = view.findViewById<View>(R.id.btn_view_recipe)

        // Snackbar when adding to favorites (course p561)
        // "Snackbar.make(view, R.string.text_label, Snackbar.LENGTH_LONG).setAction(...).show()"
        btnFavorite?.setOnClickListener {
            Snackbar.make(view, R.string.added_to_favorites, Snackbar.LENGTH_LONG)
                .setAction(R.string.snackbar_undo) {
                    // Undo action
                }
                .setActionTextColor(resources.getColor(R.color.primary_green, null))
                .show()
        }

        btnViewRecipe?.setOnClickListener {
            // TODO: Navigate to Meal Details screen
        }
    }
}

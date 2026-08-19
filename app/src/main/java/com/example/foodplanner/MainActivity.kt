package com.example.foodplanner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.foodplanner.ui.categories.CategoriesFragment
import com.example.foodplanner.ui.favorites.FavoritesFragment
import com.example.foodplanner.ui.home.HomeFragment
import com.example.foodplanner.ui.planner.PlannerFragment
import com.example.foodplanner.ui.profile.ProfileFragment
import com.example.foodplanner.ui.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * MainActivity — Engineer 1 (Lead UI/UX & Design Specialist)
 *
 * Hosts all 5 fragment destinations via BottomNavigationView.
 * All screens share the cream (#FAF9F4) background defined globally in Theme.FoodPlanner.
 *
 * Course reference:
 *   - Activity (p46-57): Extends AppCompatActivity, setContentView in onCreate
 *   - FragmentManager (course p428-429): manages fragment transactions
 *   - BottomNavigationView listener (course p559):
 *       "bottomNav.setOnItemSelectedListener { item -> when(item.itemId) {...} }"
 *   - Navigation destinations (course p557-559): up to 5 destinations
 */
class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottom_navigation)

        // Load HomeFragment as the default/start destination
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        // BottomNavigationView item selected listener (course p559)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.nav_search -> {
                    loadFragment(SearchFragment())     // Engineer 1: Search screen
                    true
                }
                R.id.nav_planner -> {
                    loadFragment(PlannerFragment())    // Engineer 1: Weekly Planner
                    true
                }
                R.id.nav_favorites -> {
                    loadFragment(FavoritesFragment())  // Engineer 1: Favorites list
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment())    // Engineer 1: Profile screen
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Replace fragment in the container using FragmentManager (course p428-429)
     * "Fragment Transactions: operations performed on the back stack"
     */
    private fun loadFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    /**
     * Programmatic navigation helper — used by ProfileFragment to switch tabs
     * (e.g., tapping "My Planner" in Profile navigates to Planner tab)
     */
    fun navigateTo(navItemId: Int) {
        bottomNav.selectedItemId = navItemId
    }
}
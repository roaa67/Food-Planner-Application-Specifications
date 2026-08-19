package com.example.foodplanner

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.foodplanner.ui.auth.SignUpActivity
import com.example.foodplanner.ui.categories.CategoriesFragment
import com.example.foodplanner.ui.favorites.FavoritesFragment
import com.example.foodplanner.ui.home.HomeFragment
import com.example.foodplanner.ui.planner.PlannerFragment
import com.example.foodplanner.ui.profile.ProfileFragment
import com.example.foodplanner.ui.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

/**
 * MainActivity — Engineer 1 (Lead UI/UX & Design Specialist)
 *
 * Hosts all 5 fragment destinations via BottomNavigationView.
 * All screens share the cream (#FAF9F4) background defined globally in Theme.FoodPlanner.
 *
 * GUEST MODE LOGIC:
 *   - If user entered as Guest (is_guest = true in SharedPreferences):
 *       - Home, Search → allowed (read-only browsing)
 *       - Profile → redirected to SignUpActivity (Create Account page)
 *       - Favorites → show Snackbar: "Create an account to save your favorites"
 *       - Planner  → show Snackbar: "Create an account to plan your meals"
 *
 * Course reference:
 *   - Activity (p46-57): Extends AppCompatActivity, setContentView in onCreate
 *   - FragmentManager (course p428-429): manages fragment transactions
 *   - BottomNavigationView listener (course p559)
 *   - SharedPreferences (course p504-519): read is_guest / is_logged_in
 *   - Snackbar (course p560-561): display guest restriction messages
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
                    loadFragment(SearchFragment())
                    true
                }
                R.id.nav_planner -> {
                    if (isGuestUser()) {
                        // Guest cannot access Planner — show Snackbar with Sign Up action (course p560-561)
                        showGuestSnackbar("Create an account to plan your weekly meals")
                        false  // Don't switch tab — keep current tab selected
                    } else {
                        loadFragment(PlannerFragment())
                        true
                    }
                }
                R.id.nav_favorites -> {
                    if (isGuestUser()) {
                        // Guest cannot save favorites — prompt to sign up (course p560-561)
                        showGuestSnackbar("Create an account to save your favorite meals")
                        false
                    } else {
                        loadFragment(FavoritesFragment())
                        true
                    }
                }
                R.id.nav_profile -> {
                    if (isGuestUser()) {
                        // Guest taps Profile → go directly to Sign Up (Create Account) page
                        // Course ref: Intent (p57-65), SharedPreferences guest detection (p504-519)
                        openSignUpForGuest()
                        false  // Don't switch tab — stay on Home
                    } else {
                        loadFragment(ProfileFragment())
                        true
                    }
                }
                else -> false
            }
        }
    }

    /**
     * Check if current user is a guest.
     * Reads is_guest flag saved by LoginActivity when user chose "Continue as Guest".
     * Course ref: SharedPreferences.getBoolean() p507
     */
    private fun isGuestUser(): Boolean {
        val prefs = getSharedPreferences("food_planner_prefs", MODE_PRIVATE)
        return prefs.getBoolean("is_guest", false)
    }

    /**
     * Open SignUpActivity for guest users who tap on a restricted tab.
     * The Sign Up activity allows them to create an account or go back.
     * Course ref: Intent (p57-65)
     */
    private fun openSignUpForGuest() {
        startActivity(Intent(this, SignUpActivity::class.java).apply {
            // Pass flag so SignUpActivity knows it was opened from guest Profile tap
            putExtra("FROM_GUEST_PROFILE", true)
        })
        // Don't call finish() — user can back-press to return to browsing as guest
    }

    /**
     * Show Snackbar with "Sign Up" action button for guest-restricted features.
     * Course ref: Snackbar with action button (course p560-561)
     */
    private fun showGuestSnackbar(message: String) {
        val rootView = findViewById<android.view.View>(android.R.id.content)
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
            .setAction("Sign Up") {
                openSignUpForGuest()
            }
            .setActionTextColor(getColor(R.color.primary_green))
            .show()
    }

    /**
     * Replace fragment in the container using FragmentManager (course p428-429)
     */
    private fun loadFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    /**
     * Programmatic navigation helper — used by ProfileFragment to switch tabs
     */
    fun navigateTo(navItemId: Int) {
        bottomNav.selectedItemId = navItemId
    }
}
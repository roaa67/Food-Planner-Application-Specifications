package com.example.foodplanner

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.foodplanner.ui.auth.SignUpActivity
import com.example.foodplanner.ui.favorites.FavoritesFragment
import com.example.foodplanner.ui.home.HomeFragment
import com.example.foodplanner.ui.planner.PlannerFragment
import com.example.foodplanner.ui.profile.ProfileFragment
import com.example.foodplanner.ui.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottom_navigation)

        // Start with HomeFragment
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

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
                        showGuestSnackbar(
                            "Create an account to plan your weekly meals"
                        )
                        false
                    } else {
                        loadFragment(PlannerFragment())
                        true
                    }
                }

                R.id.nav_favorites -> {
                    if (isGuestUser()) {
                        showGuestSnackbar(
                            "Create an account to save your favorite meals"
                        )
                        false
                    } else {
                        loadFragment(FavoritesFragment())
                        true
                    }
                }

                R.id.nav_profile -> {
                    if (isGuestUser()) {
                        openSignUpForGuest()
                        false
                    } else {
                        loadFragment(ProfileFragment())
                        true
                    }
                }

                else -> false
            }
        }
    }

    private fun isGuestUser(): Boolean {
        val prefs = getSharedPreferences(
            "food_planner_prefs",
            MODE_PRIVATE
        )

        return prefs.getBoolean(
            "is_guest",
            false
        )
    }

    private fun openSignUpForGuest() {
        startActivity(
            Intent(
                this,
                SignUpActivity::class.java
            ).apply {
                putExtra(
                    "FROM_GUEST_PROFILE",
                    true
                )
            }
        )
    }

    private fun showGuestSnackbar(message: String) {

        val rootView =
            findViewById<android.view.View>(
                android.R.id.content
            )

        Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_LONG
        )
            .setAction("Sign Up") {
                openSignUpForGuest()
            }
            .setActionTextColor(
                getColor(R.color.primary_green)
            )
            .show()
    }

    private fun loadFragment(
        fragment: androidx.fragment.app.Fragment
    ) {

        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container,
                fragment
            )
            .commit()
    }

    fun navigateTo(navItemId: Int) {
        bottomNav.selectedItemId = navItemId
    }
}
package com.example.foodplanner.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.foodplanner.MainActivity
import com.example.foodplanner.R
import com.example.foodplanner.databinding.FragmentProfileBinding
import com.example.foodplanner.ui.auth.LoginActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(
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

        loadUserProfile()
        setupMenuAppearance()
        setupMenuClickListeners()
    }

    private fun loadUserProfile() {

        val prefs =
            requireContext().getSharedPreferences(
                "food_planner_prefs",
                0
            )

        val name =
            prefs.getString(
                "user_name",
                "Guest User"
            ) ?: "Guest User"

        val email =
            prefs.getString(
                "user_email",
                ""
            ) ?: ""

        binding.tvProfileName.text = name

        binding.tvProfileEmail.text =
            if (email.isBlank()) {
                "Food Planner Member"
            } else {
                email
            }
    }

    private fun setupMenuAppearance() {

        // My Planner
        binding.menuMyPlanner.tvMenuLabel.text =
            "My Planner"

        binding.menuMyPlanner.ivMenuIcon.setImageResource(
            R.drawable.ic_planner
        )

        // Favorites
        binding.menuFavorites.tvMenuLabel.text =
            "Favorites"

        binding.menuFavorites.ivMenuIcon.setImageResource(
            R.drawable.ic_favorites
        )

        // My Recipes
        binding.menuMyRecipes.tvMenuLabel.text =
            "My Recipes"

        binding.menuMyRecipes.ivMenuIcon.setImageResource(
            R.drawable.ic_home
        )

        // Settings
        binding.menuSettings.tvMenuLabel.text =
            "Settings"

        binding.menuSettings.ivMenuIcon.setImageResource(
            R.drawable.ic_profile
        )

        // Help & Support
        binding.menuHelp.tvMenuLabel.text =
            "Help & Support"

        binding.menuHelp.ivMenuIcon.setImageResource(
            R.drawable.ic_search
        )

        // About
        binding.menuAbout.tvMenuLabel.text =
            "About Us"

        binding.menuAbout.ivMenuIcon.setImageResource(
            R.drawable.ic_home
        )

        // Logout
        binding.menuLogout.tvMenuLabel.text =
            "Logout"

        binding.menuLogout.ivMenuIcon.setImageResource(
            R.drawable.ic_back
        )
    }

    private fun setupMenuClickListeners() {

        binding.menuMyPlanner.root.setOnClickListener {

            val activity =
                requireActivity()

            if (activity is MainActivity) {

                activity.navigateTo(
                    R.id.nav_planner
                )
            }
        }

        binding.menuFavorites.root.setOnClickListener {

            val activity =
                requireActivity()

            if (activity is MainActivity) {

                activity.navigateTo(
                    R.id.nav_favorites
                )
            }
        }

        binding.menuMyRecipes.root
            .setOnClickListener {
                // Future feature
            }

        binding.menuSettings.root
            .setOnClickListener {
                // Future feature
            }

        binding.menuHelp.root
            .setOnClickListener {
                // Future feature
            }

        binding.menuAbout.root
            .setOnClickListener {
                // Future feature
            }

        binding.menuLogout.root
            .setOnClickListener {

                AlertDialog.Builder(
                    requireContext()
                )
                    .setTitle(
                        getString(
                            R.string.menu_logout
                        )
                    )
                    .setMessage(
                        getString(
                            R.string.logout_confirm
                        )
                    )
                    .setPositiveButton(
                        getString(
                            R.string.menu_logout
                        )
                    ) { _, _ ->

                        performLogout()
                    }
                    .setNegativeButton(
                        android.R.string.cancel,
                        null
                    )
                    .show()
            }
    }

    private fun performLogout() {

        requireContext()
            .getSharedPreferences(
                "food_planner_prefs",
                0
            )
            .edit()
            .clear()
            .apply()

        val intent =
            Intent(
                requireContext(),
                LoginActivity::class.java
            ).apply {

                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

        startActivity(intent)
    }

    override fun onDestroyView() {

        _binding = null

        super.onDestroyView()
    }
}
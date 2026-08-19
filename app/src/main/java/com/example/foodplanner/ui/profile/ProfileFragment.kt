package com.example.foodplanner.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.foodplanner.R
import com.example.foodplanner.databinding.FragmentProfileBinding
import com.example.foodplanner.ui.auth.LoginActivity

/**
 * ProfileFragment — Engineer 1 (Lead UI/UX & Design Specialist)
 *
 * Displays user profile with:
 *  - Dark green header: circular photo + name + email
 *  - White menu card: My Planner, Favorites, My Recipes, Settings, Help & Support, About Us, Logout
 *
 * Course ref: Fragment lifecycle p380-382, SharedPreferences p504-519, AlertDialog p562
 * Accessibility: All menu items >= 48dp touch target (course p192)
 */
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserProfile()
        setupMenuClickListeners()
    }

    /**
     * Load user name/email from SharedPreferences (course p504-519)
     */
    private fun loadUserProfile() {
        val prefs = requireContext().getSharedPreferences("food_planner_prefs", 0)
        val name = prefs.getString("user_name", "Guest User") ?: "Guest User"
        val email = prefs.getString("user_email", "") ?: ""
        binding.tvProfileName.text = name
        binding.tvProfileEmail.text = email
    }

    /**
     * Set up click listeners for all profile menu items
     * Each item has >= 48dp height (enforced in item_profile_menu.xml — course Accessibility p192)
     */
    private fun setupMenuClickListeners() {
        // My Planner
        binding.menuMyPlanner.root.setOnClickListener {
            // Navigate to Planner tab — handled by MainActivity BottomNav
            requireActivity().let { activity ->
                if (activity is com.example.foodplanner.MainActivity) {
                    activity.navigateTo(R.id.nav_planner)
                }
            }
        }

        // Favorites
        binding.menuFavorites.root.setOnClickListener {
            requireActivity().let { activity ->
                if (activity is com.example.foodplanner.MainActivity) {
                    activity.navigateTo(R.id.nav_favorites)
                }
            }
        }

        // My Recipes, Settings, Help — placeholders for Engineer 2
        binding.menuMyRecipes.root.setOnClickListener { /* TODO: Engineer 2 */ }
        binding.menuSettings.root.setOnClickListener { /* TODO: Engineer 2 */ }
        binding.menuHelp.root.setOnClickListener { /* TODO: Engineer 2 */ }
        binding.menuAbout.root.setOnClickListener { /* TODO: Engineer 2 */ }

        // Logout with AlertDialog confirmation (course p562)
        binding.menuLogout.root.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.menu_logout))
                .setMessage(getString(R.string.logout_confirm))
                .setPositiveButton(getString(R.string.menu_logout)) { _, _ ->
                    performLogout()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }
    }

    /**
     * Clear SharedPreferences and navigate to Login (course p504-519)
     */
    private fun performLogout() {
        requireContext().getSharedPreferences("food_planner_prefs", 0)
            .edit()
            .clear()
            .apply()

        startActivity(Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

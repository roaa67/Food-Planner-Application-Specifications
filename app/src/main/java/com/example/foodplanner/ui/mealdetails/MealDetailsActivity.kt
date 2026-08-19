package com.example.foodplanner.ui.mealdetails

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.foodplanner.R
import com.example.foodplanner.databinding.ActivityMealDetailsBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

/**
 * MealDetailsActivity — Engineer 1 (Lead UI/UX & Design Specialist)
 *
 * High-fidelity meal details layout containing:
 *  - Hero image (fullscreen with back button and heart button)
 *  - Floating info card: name + area/time/difficulty tags
 *  - TabLayout with 3 tabs: Ingredients | Steps | Video
 *  - ViewPager2 connected to tabs via TabLayoutMediator (course p502-505)
 *  - Add to Favorites button with Snackbar feedback (course p560-561)
 *
 * Accessibility:
 *  - Back button: 48dp x 48dp touch target (course p192)
 *  - Heart button: 48dp x 48dp touch target (course p192)
 *  - All images have contentDescription (course p197-198)
 */
class MealDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealId = intent.getStringExtra("MEAL_ID") ?: ""
        val mealName = intent.getStringExtra("MEAL_NAME") ?: ""
        val mealImage = intent.getStringExtra("MEAL_IMAGE") ?: ""
        val mealArea = intent.getStringExtra("MEAL_AREA") ?: ""
        val mealYoutubeUrl = intent.getStringExtra("MEAL_YOUTUBE") ?: ""

        setupHeroImage(mealImage)
        setupMealInfo(mealName, mealArea)
        setupTabs(mealId, mealYoutubeUrl)
        setupButtons(mealName)
    }

    /**
     * Load hero image with Glide (course p500-502)
     */
    private fun setupHeroImage(imageUrl: String) {
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.ic_chef_logo)
            .centerCrop()
            .into(binding.ivMealDetailImage)
    }

    private fun setupMealInfo(name: String, area: String) {
        binding.tvMealDetailName.text = name
        binding.tvMealArea.text = "🌍 $area"
        binding.tvMealTime.text = "45 min"        // Engineer 2 provides real value
        binding.tvMealDifficulty.text = "Easy"   // Engineer 2 provides real value
    }

    /**
     * Connect TabLayout to ViewPager2 using TabLayoutMediator — course p502-505
     */
    private fun setupTabs(mealId: String, youtubeUrl: String) {
        val tabTitles = listOf(
            getString(R.string.tab_ingredients),
            getString(R.string.tab_steps),
            getString(R.string.tab_video)
        )

        val pagerAdapter = MealTabsPagerAdapter(this, mealId, youtubeUrl)
        binding.vp2MealTabs.adapter = pagerAdapter
        binding.vp2MealTabs.isUserInputEnabled = true

        TabLayoutMediator(binding.tabLayoutMeal, binding.vp2MealTabs) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun setupButtons(mealName: String) {
        // Back button
        binding.btnBackMeal.setOnClickListener { finish() }

        // Toggle favorite heart button
        var isFavorite = false
        binding.btnMealFavorite.setOnClickListener {
            isFavorite = !isFavorite
            if (isFavorite) {
                binding.btnMealFavorite.setImageResource(R.drawable.ic_favorites)
                // TODO: Engineer 2 saves to Room DB
                Snackbar.make(binding.root, getString(R.string.added_to_favorites), Snackbar.LENGTH_SHORT).show()
            } else {
                binding.btnMealFavorite.setImageResource(R.drawable.ic_favorites)
                // TODO: Engineer 2 removes from Room DB
                Snackbar.make(binding.root, getString(R.string.removed_from_favorites), Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.snackbar_undo)) {
                        isFavorite = true
                        // TODO: Engineer 2 restores
                    }.show()
            }
        }

        // Add to Favorites button
        binding.btnAddToFavorites.setOnClickListener {
            Snackbar.make(
                binding.root,
                getString(R.string.added_to_favorites),
                Snackbar.LENGTH_LONG
            ).setAction(getString(R.string.snackbar_undo)) {
                // TODO: Engineer 2 handles Room DB undo
            }.show()
        }
    }
}

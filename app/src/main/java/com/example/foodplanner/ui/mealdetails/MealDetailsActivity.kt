package com.example.foodplanner.ui.mealdetails

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.foodplanner.R
import com.example.foodplanner.databinding.ActivityMealDetailsBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class MealDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            ActivityMealDetailsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val mealId =
            intent.getStringExtra("MEAL_ID").orEmpty()

        val mealName =
            intent.getStringExtra("MEAL_NAME").orEmpty()

        val mealImage =
            intent.getStringExtra("MEAL_IMAGE").orEmpty()

        val mealArea =
            intent.getStringExtra("MEAL_AREA").orEmpty()

        val mealYoutubeUrl =
            intent.getStringExtra("MEAL_YOUTUBE").orEmpty()

        setupHeroImage(mealImage)

        setupMealInfo(
            mealName,
            mealArea
        )

        setupTabs(
            mealId,
            mealYoutubeUrl
        )

        setupButtons()
    }

    // =====================================================
    // Hero Image
    // =====================================================

    private fun setupHeroImage(
        imageUrl: String
    ) {

        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.ic_chef_logo)
            .error(R.drawable.ic_chef_logo)
            .centerCrop()
            .into(binding.ivMealDetailImage)
    }

    // =====================================================
    // Meal Information
    // =====================================================

    private fun setupMealInfo(
        name: String,
        area: String
    ) {

        // Meal name
        binding.tvMealDetailName.text =
            if (name.isBlank()) {
                "Meal"
            } else {
                name
            }

        // Area / Country
        if (area.isNotBlank()) {

            binding.tvMealArea.visibility =
                View.VISIBLE

            binding.tvMealArea.text =
                "🌍 $area"

        } else {

            binding.tvMealArea.visibility =
                View.GONE
        }

        /*
         * TheMealDB API does not provide
         * real cooking time or difficulty.
         *
         * لذلك مش هنظهر بيانات وهمية
         * زي 45 min أو Easy.
         */
        binding.tvMealTime.visibility =
            View.GONE

        binding.tvMealDifficulty.visibility =
            View.GONE
    }

    // =====================================================
    // Tabs
    // =====================================================

    private fun setupTabs(
        mealId: String,
        youtubeUrl: String
    ) {

        val tabTitles =
            listOf(
                getString(
                    R.string.tab_ingredients
                ),
                getString(
                    R.string.tab_steps
                ),
                getString(
                    R.string.tab_video
                )
            )

        val pagerAdapter =
            MealTabsPagerAdapter(
                this,
                mealId,
                youtubeUrl
            )

        binding.vp2MealTabs.adapter =
            pagerAdapter

        binding.vp2MealTabs.isUserInputEnabled =
            true

        TabLayoutMediator(
            binding.tabLayoutMeal,
            binding.vp2MealTabs
        ) { tab, position ->

            tab.text =
                tabTitles[position]

        }.attach()
    }

    // =====================================================
    // Buttons
    // =====================================================

    private fun setupButtons() {

        // Back
        binding.btnBackMeal
            .setOnClickListener {

                finish()
            }

        // -----------------------------------------------
        // Favorite
        // -----------------------------------------------

        var isFavorite = false

        binding.btnMealFavorite
            .setOnClickListener {

                isFavorite =
                    !isFavorite

                if (isFavorite) {

                    binding.btnMealFavorite
                        .setImageResource(
                            R.drawable.ic_favorites
                        )

                    Snackbar.make(
                        binding.root,
                        getString(
                            R.string.added_to_favorites
                        ),
                        Snackbar.LENGTH_SHORT
                    ).show()

                    /*
                     * TODO:
                     * Room integration
                     * will save meal here.
                     */

                } else {

                    binding.btnMealFavorite
                        .setImageResource(
                            R.drawable.ic_favorites
                        )

                    Snackbar.make(
                        binding.root,
                        getString(
                            R.string.removed_from_favorites
                        ),
                        Snackbar.LENGTH_SHORT
                    )
                        .setAction(
                            getString(
                                R.string.snackbar_undo
                            )
                        ) {

                            isFavorite = true

                            binding.btnMealFavorite
                                .setImageResource(
                                    R.drawable.ic_favorites
                                )

                            /*
                             * TODO:
                             * Restore favorite in Room.
                             */
                        }
                        .show()
                }
            }
    }
}
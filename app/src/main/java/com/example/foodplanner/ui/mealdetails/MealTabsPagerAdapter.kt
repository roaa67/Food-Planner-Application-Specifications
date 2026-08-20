package com.example.foodplanner.ui.mealdetails

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * MealTabsPagerAdapter — Engineer 1 (Lead UI/UX)
 * FragmentStateAdapter for ViewPager2 — holds 3 tab fragments:
 *   0 = IngredientsFragment
 *   1 = StepsFragment
 *   2 = VideoFragment
 *
 * Course ref: ViewPager2 + FragmentStateAdapter p498-505
 */
class MealTabsPagerAdapter(
    activity: FragmentActivity,
    private val mealId: String,
    private val youtubeUrl: String
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> IngredientsFragment.newInstance(mealId)
            1 -> StepsFragment.newInstance(mealId)
            2 -> VideoFragment.newInstance(youtubeUrl)
            else -> IngredientsFragment.newInstance(mealId)
        }
    }
}

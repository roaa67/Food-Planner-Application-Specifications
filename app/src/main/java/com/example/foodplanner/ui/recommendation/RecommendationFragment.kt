package com.example.foodplanner.ui.recommendation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.foodplanner.R
import com.example.foodplanner.databinding.FragmentRecommendationBinding
import com.example.foodplanner.ui.mealdetails.MealDetailsActivity
import kotlin.math.abs

/**
 * RecommendationItem — Data model for recommendation cards
 */
data class RecommendationItem(
    val id: String,
    val name: String,
    val flag: String,
    val country: String,
    val imageUrl: String,
    val youtubeUrl: String = ""
)

/**
 * RecommendationFragment — Engineer 1 (Lead UI/UX & Design Specialist)
 *
 * Implements "Daily Recommendation" (For You) screen matching mockup media_1787101644465.png:
 *   - Top bar: Drawer menu icon + "Daily Recommendation" title + Bell icon
 *   - MaterialCardView: 🌿 Today's Recommendation 🌿
 *   - Meal Name + Country Flag + Meal Image
 *   - Pagination Dots indicator (● ○ ○ ○)
 *   - Touch & Arrow Navigation:
 *       * Swipe Right (or click → arrow / tap card) → Navigates to MealDetailsActivity (Recipe details)
 *       * Swipe Left (or click ← arrow) → Switches to next recommendation card with animation!
 *
 * Course ref: Fragment lifecycle p380, Gesture handling, ViewBinding p395-402, Intent p243
 */
class RecommendationFragment : Fragment() {

    private var _binding: FragmentRecommendationBinding? = null
    private val binding get() = _binding!!

    private var currentIndex = 0
    private lateinit var recommendations: List<RecommendationItem>
    private lateinit var gestureDetector: GestureDetector

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecommendationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadMockRecommendations()
        setupGestureDetector()
        setupClickListeners()
        displayCard(currentIndex)
    }

    /**
     * Load mock recommendation items — Engineer 2 will inject live API data here
     */
    private fun loadMockRecommendations() {
        recommendations = listOf(
            RecommendationItem(
                id = "52772",
                name = "Spicy Lemon Chicken",
                flag = "🇹🇭",
                country = "Thailand",
                imageUrl = "https://www.themealdb.com/images/media/meals/z0ageb1583189517.jpg",
                youtubeUrl = "https://www.youtube.com/watch?v=1IszT_guI08"
            ),
            RecommendationItem(
                id = "52771",
                name = "Chicken Alfredo Pasta",
                flag = "🇮🇹",
                country = "Italian",
                imageUrl = "https://www.themealdb.com/images/media/meals/syqypv1486981727.jpg",
                youtubeUrl = "https://www.youtube.com/watch?v=843U7aY4Y_g"
            ),
            RecommendationItem(
                id = "52773",
                name = "Tacos al Pastor",
                flag = "🇲🇽",
                country = "Mexican",
                imageUrl = "https://www.themealdb.com/images/media/meals/uvuqqw1511516084.jpg"
            ),
            RecommendationItem(
                id = "52774",
                name = "Teriyaki Chicken",
                flag = "🇯🇵",
                country = "Japanese",
                imageUrl = "https://www.themealdb.com/images/media/meals/wvpsxx1468256321.jpg"
            )
        )
    }

    /**
     * Update card UI for current index
     */
    private fun displayCard(index: Int) {
        if (recommendations.isEmpty()) return

        val item = recommendations[index]
        binding.tvRecMealName.text = item.name
        binding.tvRecFlag.text = item.flag
        binding.tvRecCountry.text = item.country

        // Load image with Glide (course p633-637)
        Glide.with(requireContext())
            .load(item.imageUrl)
            .placeholder(R.drawable.ic_chef_logo)
            .centerCrop()
            .into(binding.ivRecMealImage)

        updateDots(index)
    }

    /**
     * Update pagination dots (● ○ ○ ○)
     */
    private fun updateDots(activeIndex: Int) {
        val dots = listOf(binding.dot0, binding.dot1, binding.dot2, binding.dot3)
        dots.forEachIndexed { idx, dot ->
            dot.setBackgroundResource(
                if (idx == activeIndex) R.drawable.bg_dot_active else R.drawable.bg_dot_inactive
            )
        }
    }

    /**
     * Next recommendation card (Swipe Left or Click ←)
     */
    private fun showNextCard() {
        currentIndex = (currentIndex + 1) % recommendations.size
        // Add card slide animation
        binding.cardRecommendation.startAnimation(
            AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_in)
        )
        displayCard(currentIndex)
    }

    /**
     * Open Recipe Details for current card (Swipe Right or Click → or Tap Card)
     */
    private fun openRecipeDetails() {
        val item = recommendations[currentIndex]
        val intent = Intent(requireContext(), MealDetailsActivity::class.java).apply {
            putExtra("MEAL_ID", item.id)
            putExtra("MEAL_NAME", item.name)
            putExtra("MEAL_IMAGE", item.imageUrl)
            putExtra("MEAL_AREA", item.country)
            putExtra("MEAL_YOUTUBE", item.youtubeUrl)
        }
        startActivity(intent)
    }

    /**
     * Set up touch gesture listener on recommendation card:
     *   - Swipe Right -> open recipe details
     *   - Swipe Left -> next card
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun setupGestureDetector() {
        gestureDetector = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
            private val SWIPE_THRESHOLD = 100
            private val SWIPE_VELOCITY_THRESHOLD = 100

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (e1 == null) return false
                val diffX = e2.x - e1.x
                val diffY = e2.y - e1.y

                if (abs(diffX) > abs(diffY) &&
                    abs(diffX) > SWIPE_THRESHOLD &&
                    abs(velocityX) > SWIPE_VELOCITY_THRESHOLD
                ) {
                    if (diffX > 0) {
                        // Swipe Right -> Open Recipe Details
                        openRecipeDetails()
                    } else {
                        // Swipe Left -> Next Card
                        showNextCard()
                    }
                    return true
                }
                return false
            }

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                openRecipeDetails()
                return true
            }
        })

        binding.cardRecommendation.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    private fun setupClickListeners() {
        // Arrow Left (Swipe Left equivalent -> next recommendation card)
        binding.btnSwipeLeft.setOnClickListener {
            showNextCard()
        }

        // Arrow Right (Swipe Right equivalent -> open recipe details)
        binding.btnSwipeRight.setOnClickListener {
            openRecipeDetails()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

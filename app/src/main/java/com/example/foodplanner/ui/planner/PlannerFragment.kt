package com.example.foodplanner.ui.planner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.foodplanner.R
import com.example.foodplanner.databinding.FragmentPlannerBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * PlannerFragment — Engineer 1 (Lead UI/UX & Design Specialist)
 *
 * Self-contained single-page Planner matching mockup media_1787127362856.png:
 *   - Top header with back button & "My Planner" title
 *   - Interactive week date navigation (< Aug 17 – Aug 23 >) with Calendar API
 *   - 7 Day rows (Mon–Sun) with thumbnails loaded via Glide
 *   - Bottom green Add Meal button
 *
 * Course ref: Fragment lifecycle p380, Glide p633-637, Calendar API
 */
class PlannerFragment : Fragment() {

    private var _binding: FragmentPlannerBinding? = null
    private val binding get() = _binding!!

    private var currentWeekCalendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentWeekCalendar.firstDayOfWeek = Calendar.MONDAY
        currentWeekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        setupWeekNavigation()
        loadMealThumbnails()
        setupClickListeners()
        updateWeekDisplay()
    }

    /**
     * Interactive week date navigation (< Prev / Next >)
     */
    private fun setupWeekNavigation() {
        binding.btnBackPlanner.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnPrevWeek.setOnClickListener {
            currentWeekCalendar.add(Calendar.WEEK_OF_YEAR, -1)
            updateWeekDisplay()
        }

        binding.btnNextWeek.setOnClickListener {
            currentWeekCalendar.add(Calendar.WEEK_OF_YEAR, 1)
            updateWeekDisplay()
        }
    }

    /**
     * Calculates date range dynamically (e.g. Aug 17 – Aug 23)
     */
    private fun updateWeekDisplay() {
        val dateFormat = SimpleDateFormat("MMM d", Locale.ENGLISH)
        val startOfWeek = currentWeekCalendar.clone() as Calendar
        val endOfWeek = currentWeekCalendar.clone() as Calendar
        endOfWeek.add(Calendar.DAY_OF_WEEK, 6)

        val rangeText = "${dateFormat.format(startOfWeek.time)} – ${dateFormat.format(endOfWeek.time)}"
        binding.tvWeekRange.text = rangeText
    }

    /**
     * Load meal thumbnails with Glide (course p633-637)
     */
    private fun loadMealThumbnails() {
        val meals = listOf(
            Pair(binding.ivMonMeal, "https://www.themealdb.com/images/media/meals/z0ageb1583189517.jpg"),
            Pair(binding.ivTueMeal, "https://www.themealdb.com/images/media/meals/syqypv1486981727.jpg"),
            Pair(binding.ivWedMeal, "https://www.themealdb.com/images/media/meals/1529444830.jpg"),
            Pair(binding.ivThuMeal, "https://www.themealdb.com/images/media/meals/1529446137.jpg"),
            Pair(binding.ivFriMeal, "https://www.themealdb.com/images/media/meals/1549542994.jpg"),
            Pair(binding.ivSatMeal, "https://www.themealdb.com/images/media/meals/wvpsxx1468256321.jpg")
        )

        meals.forEach { (imageView, url) ->
            Glide.with(requireContext())
                .load(url)
                .placeholder(R.drawable.ic_chef_logo)
                .centerCrop()
                .into(imageView)
        }
    }

    private fun setupClickListeners() {
        binding.cardSunAdd.setOnClickListener {
            Snackbar.make(binding.root, "Add meal for Sunday", Snackbar.LENGTH_SHORT).show()
        }

        binding.btnAddMealMain.setOnClickListener {
            Snackbar.make(binding.root, "Add meal to weekly planner", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

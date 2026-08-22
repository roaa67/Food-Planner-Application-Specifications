package com.example.foodplanner.ui.planner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foodplanner.databinding.FragmentPlannerBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PlannerFragment : Fragment() {

    private var _binding: FragmentPlannerBinding? = null
    private val binding get() = _binding!!

    private var currentWeekCalendar: Calendar =
        Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            FragmentPlannerBinding.inflate(
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
        super.onViewCreated(
            view,
            savedInstanceState
        )

        setupCurrentWeek()

        setupWeekNavigation()

        setupClickListeners()

        updateWeekDisplay()
    }

    // =====================================================
    // Current Week
    // =====================================================

    private fun setupCurrentWeek() {

        currentWeekCalendar.firstDayOfWeek =
            Calendar.MONDAY

        currentWeekCalendar.set(
            Calendar.DAY_OF_WEEK,
            Calendar.MONDAY
        )
    }

    // =====================================================
    // Week Navigation
    // =====================================================

    private fun setupWeekNavigation() {

        // Back
        binding.btnBackPlanner
            .setOnClickListener {

                requireActivity()
                    .onBackPressedDispatcher
                    .onBackPressed()
            }

        // Previous week
        binding.btnPrevWeek
            .setOnClickListener {

                currentWeekCalendar.add(
                    Calendar.WEEK_OF_YEAR,
                    -1
                )

                updateWeekDisplay()
            }

        // Next week
        binding.btnNextWeek
            .setOnClickListener {

                currentWeekCalendar.add(
                    Calendar.WEEK_OF_YEAR,
                    1
                )

                updateWeekDisplay()
            }
    }

    // =====================================================
    // Week Range
    // =====================================================

    private fun updateWeekDisplay() {

        val dateFormat =
            SimpleDateFormat(
                "MMM d",
                Locale.ENGLISH
            )

        val startOfWeek =
            currentWeekCalendar.clone()
                    as Calendar

        val endOfWeek =
            currentWeekCalendar.clone()
                    as Calendar

        endOfWeek.add(
            Calendar.DAY_OF_WEEK,
            6
        )

        val startText =
            dateFormat.format(
                startOfWeek.time
            )

        val endText =
            dateFormat.format(
                endOfWeek.time
            )

        binding.tvWeekRange.text =
            "$startText – $endText"
    }

    // =====================================================
    // Click Listeners
    // =====================================================

    private fun setupClickListeners() {

        // Sunday Add Meal
        binding.cardSunAdd
            .setOnClickListener {

                showAddMealMessage(
                    "Sunday"
                )
            }

        // Main Add Meal Button
        binding.btnAddMealMain
            .setOnClickListener {

                Snackbar.make(
                    binding.root,
                    "Choose a day to add a meal",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
    }

    // =====================================================
    // Add Meal Message
    // =====================================================

    private fun showAddMealMessage(
        day: String
    ) {

        Snackbar.make(
            binding.root,
            "Add meal for $day",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    // =====================================================
    // Destroy View
    // =====================================================

    override fun onDestroyView() {

        _binding = null

        super.onDestroyView()
    }
}
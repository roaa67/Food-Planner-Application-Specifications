package com.example.foodplanner.ui.planner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodplanner.databinding.FragmentPlannerBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * PlannerFragment — Engineer 1 (Lead UI/UX & Design Specialist)
 *
 * Single interactive Planner page matching mockup media_1787127362856.png:
 *   - Top header with back button & "My Planner" title
 *   - Interactive week date navigation (< Aug 17 – Aug 23 >) calculating week dates dynamically using Calendar API
 *   - 7 Day rows (Mon-Sun) with planned meals & thumbnails
 *   - Bottom green Add Meal button
 *
 * Course ref: Fragment lifecycle p380, RecyclerView p330-335, Calendar API
 */
class PlannerFragment : Fragment() {

    private var _binding: FragmentPlannerBinding? = null
    private val binding get() = _binding!!

    private lateinit var plannerAdapter: PlannerAdapter
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

        // Set calendar to Monday of current week
        currentWeekCalendar.firstDayOfWeek = Calendar.MONDAY
        currentWeekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        setupRecyclerView()
        setupWeekNavigation()
        setupAddMealButton()
        updateWeekDisplay()
    }

    private fun setupRecyclerView() {
        plannerAdapter = PlannerAdapter(getMockWeekData()) { day ->
            Snackbar.make(binding.root, "Select meal for ${day.dayName}", Snackbar.LENGTH_SHORT).show()
        }
        binding.rvPlannerDays.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = plannerAdapter
        }
    }

    /**
     * Week navigation function: < Prev / Next >
     * Calculates date range dynamically (e.g. Aug 17 – Aug 23)
     */
    private fun setupWeekNavigation() {
        // Back arrow top bar
        binding.btnBackPlanner.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Previous week arrow (<)
        binding.btnPrevWeek.setOnClickListener {
            currentWeekCalendar.add(Calendar.WEEK_OF_YEAR, -1)
            updateWeekDisplay()
        }

        // Next week arrow (>)
        binding.btnNextWeek.setOnClickListener {
            currentWeekCalendar.add(Calendar.WEEK_OF_YEAR, 1)
            updateWeekDisplay()
        }
    }

    /**
     * Calculates and formats week range text (e.g. Aug 17 – Aug 23)
     */
    private fun updateWeekDisplay() {
        val dateFormat = SimpleDateFormat("MMM d", Locale.ENGLISH)
        val startOfWeek = currentWeekCalendar.clone() as Calendar
        val endOfWeek = currentWeekCalendar.clone() as Calendar
        endOfWeek.add(Calendar.DAY_OF_WEEK, 6)

        val rangeText = "${dateFormat.format(startOfWeek.time)} – ${dateFormat.format(endOfWeek.time)}"
        binding.tvWeekRange.text = rangeText
    }

    private fun setupAddMealButton() {
        binding.btnAddMealMain.setOnClickListener {
            Snackbar.make(binding.root, "Add meal to weekly planner", Snackbar.LENGTH_SHORT).show()
        }
    }

    /**
     * Mock planned meals for 7 days matching design mockup media_1787127362856.png
     */
    private fun getMockWeekData(): List<DayItem> {
        return listOf(
            DayItem("Mon", "Grilled Lemon Chicken", "https://www.themealdb.com/images/media/meals/z0ageb1583189517.jpg"),
            DayItem("Tue", "Pasta Primavera", "https://www.themealdb.com/images/media/meals/syqypv1486981727.jpg"),
            DayItem("Wed", "Beef Stir Fry", "https://www.themealdb.com/images/media/meals/1529444830.jpg"),
            DayItem("Thu", "Tomato Soup", "https://www.themealdb.com/images/media/meals/1529446137.jpg"),
            DayItem("Fri", "Baked Salmon", "https://www.themealdb.com/images/media/meals/1549542994.jpg"),
            DayItem("Sat", "Caesar Salad", "https://www.themealdb.com/images/media/meals/wvpsxx1468256321.jpg"),
            DayItem("Sun", null, null) // Empty slot: "Add Meal" + plus icon
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

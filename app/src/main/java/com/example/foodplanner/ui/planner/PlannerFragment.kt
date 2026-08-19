package com.example.foodplanner.ui.planner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodplanner.databinding.FragmentPlannerBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * PlannerFragment — Engineer 1 (Lead UI/UX & Design Specialist)
 *
 * Weekly meal planner with:
 *  - < Prev / Next > week navigation
 *  - Mon–Sun rows, each showing planned meal or + Add Meal slot
 *
 * Course ref: Fragment lifecycle p380, RecyclerView p330-335, Calendar API
 */
class PlannerFragment : Fragment() {

    private var _binding: FragmentPlannerBinding? = null
    private val binding get() = _binding!!

    private lateinit var plannerAdapter: PlannerAdapter
    private var currentWeekStart: Calendar = Calendar.getInstance()

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
        // Set to Monday of current week
        currentWeekStart.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        setupRecyclerView()
        updateWeekDisplay()
        setupWeekNavigation()
    }

    private fun setupRecyclerView() {
        plannerAdapter = PlannerAdapter(getWeekDayItems()) { day ->
            // TODO: Engineer 2 shows meal picker dialog for this day
        }
        binding.rvPlannerDays.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = plannerAdapter
        }
    }

    /**
     * Displays the week range label: e.g. "Aug 17 – Aug 23"
     */
    private fun updateWeekDisplay() {
        val fmt = SimpleDateFormat("MMM d", Locale.getDefault())
        val weekEnd = currentWeekStart.clone() as Calendar
        weekEnd.add(Calendar.DAY_OF_WEEK, 6)
        binding.tvWeekRange.text = "${fmt.format(currentWeekStart.time)} – ${fmt.format(weekEnd.time)}"
        plannerAdapter.updateData(getWeekDayItems())
    }

    private fun setupWeekNavigation() {
        binding.btnPrevWeek.setOnClickListener {
            currentWeekStart.add(Calendar.WEEK_OF_YEAR, -1)
            updateWeekDisplay()
        }
        binding.btnNextWeek.setOnClickListener {
            currentWeekStart.add(Calendar.WEEK_OF_YEAR, 1)
            updateWeekDisplay()
        }
    }

    /**
     * Generates 7 DayItem entries (Mon–Sun) for the current week
     */
    private fun getWeekDayItems(): List<DayItem> {
        val dayNames = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        return dayNames.map { DayItem(it, null, null) }
        // Engineer 2 will populate meal names and images from Room DB PlannedMeal table
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

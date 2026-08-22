package com.example.foodplanner.ui.mealdetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodplanner.RetrofitClient
import com.example.foodplanner.databinding.FragmentMealStepsBinding
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class StepsFragment : Fragment() {

    private var _binding: FragmentMealStepsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: StepsAdapter

    private val disposables = CompositeDisposable()

    companion object {

        private const val ARG_MEAL_ID = "MEAL_ID"

        fun newInstance(mealId: String): StepsFragment {
            return StepsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MEAL_ID, mealId)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMealStepsBinding.inflate(
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

        setupRecyclerView()

        val mealId =
            arguments?.getString(ARG_MEAL_ID).orEmpty()

        if (mealId.isNotBlank()) {
            loadSteps(mealId)
        }
    }

    private fun setupRecyclerView() {

        // Create adapter only once
        adapter = StepsAdapter(emptyList())

        binding.rvSteps.apply {

            layoutManager =
                LinearLayoutManager(requireContext())

            adapter = this@StepsFragment.adapter

            // RecyclerView handles scrolling
            isNestedScrollingEnabled = true

            // Step cards have different heights
            setHasFixedSize(false)

            // Prevent animation/layout issues
            itemAnimator = null
        }
    }

    private fun loadSteps(mealId: String) {

        val disposable =
            RetrofitClient.apiService
                .getMealDetails(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->

                        // Fragment may have been destroyed
                        if (_binding == null) {
                            return@subscribe
                        }

                        val meal =
                            response.meals?.firstOrNull()

                        val instructions =
                            meal?.strInstructions.orEmpty()

                        val steps =
                            buildStepsList(instructions)

                        // IMPORTANT:
                        // Update existing adapter instead of creating a new one
                        adapter.updateData(steps)

                        Log.d(
                            "StepsFragment",
                            "Loaded ${steps.size} cooking steps"
                        )
                    },
                    { error ->

                        Log.e(
                            "StepsFragment",
                            "Steps API Error",
                            error
                        )

                        if (_binding != null && isAdded) {

                            Snackbar.make(
                                binding.root,
                                "Failed to load cooking steps",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                )

        disposables.add(disposable)
    }

    private fun buildStepsList(
        instructions: String
    ): List<StepItem> {

        if (instructions.isBlank()) {
            return emptyList()
        }

        val cleanedInstructions =
            instructions
                .replace("\r\n", "\n")
                .replace("\r", "\n")
                .trim()

        val rawSteps =
            cleanedInstructions
                .split(
                    Regex(
                        """\n+|(?<=[.!?])\s+(?=[A-Z])"""
                    )
                )
                .map { step ->
                    step.trim()
                }
                .filter { step ->
                    step.isNotBlank()
                }

        return rawSteps.mapIndexed { index, instruction ->

            StepItem(
                number = index + 1,
                text = instruction
            )
        }
    }

    override fun onDestroyView() {

        // Cancel active API subscriptions
        disposables.clear()

        // Remove adapter reference from RecyclerView
        if (_binding != null) {
            binding.rvSteps.adapter = null
        }

        _binding = null

        super.onDestroyView()
    }
}
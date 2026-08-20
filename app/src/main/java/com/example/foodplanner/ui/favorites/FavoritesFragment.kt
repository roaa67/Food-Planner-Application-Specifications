package com.example.foodplanner.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodplanner.databinding.FragmentFavoritesBinding

/**
 * FavoritesFragment — Engineer 1 (Lead UI/UX & Design Specialist)
 *
 * Displays saved/favorited meals as a scrollable vertical list.
 * Shows empty state view when list is empty (using visibility toggle — course p232 Group widget)
 * Shows Snackbar with UNDO on remove (course p560-561)
 *
 * Course ref: RecyclerView p330-335, Snackbar p560-561, Fragment lifecycle p380-382
 */
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeFavorites()
    }

    private fun setupRecyclerView() {
        favoritesAdapter = FavoritesAdapter(mutableListOf()) { meal ->
            // Snackbar with UNDO (course p560-561) — Engineer 1 handles UI; Engineer 2 handles DB
            com.google.android.material.snackbar.Snackbar
                .make(
                    binding.root,
                    getString(com.example.foodplanner.R.string.removed_from_favorites),
                    com.google.android.material.snackbar.Snackbar.LENGTH_LONG
                )
                .setAction(getString(com.example.foodplanner.R.string.snackbar_undo)) {
                    // Undo remove — Engineer 2 handles Room DB restore
                }
                .show()
        }

        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoritesAdapter
        }
    }

    /**
     * Engineer 2 will call favoritesAdapter.updateData(meals) here via LiveData/ViewModel
     * For now, show placeholder to demonstrate empty vs filled state.
     */
    private fun observeFavorites() {
        val hasFavorites = false // Engineer 2 replaces with Room LiveData
        if (hasFavorites) {
            binding.rvFavorites.visibility = View.VISIBLE
            binding.layoutEmptyFavorites.visibility = View.GONE
        } else {
            binding.rvFavorites.visibility = View.GONE
            binding.layoutEmptyFavorites.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

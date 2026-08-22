package com.example.foodplanner.ui.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodplanner.R
import com.example.foodplanner.RetrofitClient
import com.example.foodplanner.databinding.FragmentCategoriesBinding
import com.example.foodplanner.ui.search.SearchFragment
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoriesGridAdapter: CategoriesGridAdapter

    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCategoriesBinding.inflate(
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
        loadCategoriesFromApi()
    }

    private fun setupRecyclerView() {

        categoriesGridAdapter =
            CategoriesGridAdapter(
                emptyList()
            ) { category ->

                Log.d(
                    "CategoriesFragment",
                    "Selected category: ${category.name}"
                )

                openSearchByCategory(
                    category.name
                )
            }

        binding.rvCategoriesGrid.apply {

            layoutManager =
                GridLayoutManager(
                    requireContext(),
                    2
                )

            adapter =
                categoriesGridAdapter
        }
    }

    private fun loadCategoriesFromApi() {

        val disposable =
            RetrofitClient.apiService
                .getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->

                        val categories =
                            response.categories
                                ?.map { category ->

                                    CategoryGridItem(
                                        name =
                                            category.strCategory.orEmpty(),

                                        mealCount =
                                            "View meals",

                                        imageUrl =
                                            category.strCategoryThumb.orEmpty()
                                    )
                                }
                                ?: emptyList()

                        categoriesGridAdapter.updateData(
                            categories
                        )
                    },
                    { error ->

                        Log.e(
                            "CategoriesFragment",
                            "Categories API Error",
                            error
                        )

                        if (isAdded) {

                            Snackbar.make(
                                binding.root,
                                "Failed to load categories",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                )

        disposables.add(disposable)
    }

    private fun openSearchByCategory(
        categoryName: String
    ) {

        val searchFragment =
            SearchFragment().apply {

                arguments = Bundle().apply {

                    putString(
                        "SEARCH_MODE",
                        "CATEGORY"
                    )

                    putString(
                        "SEARCH_VALUE",
                        categoryName
                    )
                }
            }

        parentFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container,
                searchFragment
            )
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {

        disposables.clear()

        _binding = null

        super.onDestroyView()
    }
}
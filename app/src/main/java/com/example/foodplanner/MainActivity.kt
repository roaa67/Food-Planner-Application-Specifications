package com.example.foodplanner

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnTestApi = findViewById<Button>(R.id.btnTestApi)
        val btnCategories = findViewById<Button>(R.id.btnCategories)
        val btnCountries = findViewById<Button>(R.id.btnCountries)
        val btnIngredients = findViewById<Button>(R.id.btnIngredients)

        val etCategory = findViewById<EditText>(R.id.etCategory)
        val btnSearchCategory = findViewById<Button>(R.id.btnSearchCategory)

        val etCountry = findViewById<EditText>(R.id.etCountry)
        val btnSearchCountry = findViewById<Button>(R.id.btnSearchCountry)

        val etIngredient = findViewById<EditText>(R.id.etIngredient)
        val btnSearchIngredient = findViewById<Button>(R.id.btnSearchIngredient)

        val etMealName = findViewById<EditText>(R.id.etMealName)
        val btnSearchMealName = findViewById<Button>(R.id.btnSearchMealName)

        val etMealId = findViewById<EditText>(R.id.etMealId)
        val btnMealDetails = findViewById<Button>(R.id.btnMealDetails)

        val tvResult = findViewById<TextView>(R.id.tvResult)

        val repository = MealRepository()

        btnTestApi.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val response = repository.getMealOfTheDay()
                    val meal = response.meals?.firstOrNull()

                    if (meal != null) {
                        tvResult.text =
                            "${meal.strMeal}\n${meal.strCategory}\n${meal.strArea}"
                    } else {
                        tvResult.text = "No meal found"
                    }
                } catch (e: Exception) {
                    tvResult.text = e.message
                }
            }
        }

        btnCategories.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val response = repository.getCategories()
                    val categories = response.categories

                    if (!categories.isNullOrEmpty()) {
                        tvResult.text = categories.joinToString("\n") {
                            it.strCategory ?: ""
                        }
                    } else {
                        tvResult.text = "No categories found"
                    }
                } catch (e: Exception) {
                    tvResult.text = e.message
                }
            }
        }

        btnCountries.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val response = repository.getAreas()
                    val areas = response.meals

                    if (!areas.isNullOrEmpty()) {
                        tvResult.text = areas.joinToString("\n") {
                            it.strArea ?: ""
                        }
                    } else {
                        tvResult.text = "No countries found"
                    }
                } catch (e: Exception) {
                    tvResult.text = e.message
                }
            }
        }

        btnIngredients.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val response = repository.getIngredients()
                    val ingredients = response.meals

                    if (!ingredients.isNullOrEmpty()) {
                        tvResult.text = ingredients
                            .take(30)
                            .joinToString("\n") {
                                it.strIngredient ?: ""
                            }
                    } else {
                        tvResult.text = "No ingredients found"
                    }
                } catch (e: Exception) {
                    tvResult.text = e.message
                }
            }
        }

        btnSearchCategory.setOnClickListener {
            val category = etCategory.text.toString().trim()

            if (category.isEmpty()) {
                tvResult.text = "Enter a category"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = repository.getMealsByCategory(category)
                    val meals = response.meals

                    if (!meals.isNullOrEmpty()) {
                        tvResult.text = meals.joinToString("\n") {
                            it.strMeal ?: ""
                        }
                    } else {
                        tvResult.text = "No meals found"
                    }
                } catch (e: Exception) {
                    tvResult.text = e.message
                }
            }
        }

        btnSearchCountry.setOnClickListener {
            val country = etCountry.text.toString().trim()

            if (country.isEmpty()) {
                tvResult.text = "Enter a country"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = repository.getMealsByArea(country)
                    val meals = response.meals

                    if (!meals.isNullOrEmpty()) {
                        tvResult.text = meals.joinToString("\n") {
                            it.strMeal ?: ""
                        }
                    } else {
                        tvResult.text = "No meals found"
                    }
                } catch (e: Exception) {
                    tvResult.text = e.message
                }
            }
        }

        btnSearchIngredient.setOnClickListener {
            val ingredient = etIngredient.text.toString().trim()

            if (ingredient.isEmpty()) {
                tvResult.text = "Enter an ingredient"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = repository.getMealsByIngredient(ingredient)
                    val meals = response.meals

                    if (!meals.isNullOrEmpty()) {
                        tvResult.text = meals.joinToString("\n") {
                            it.strMeal ?: ""
                        }
                    } else {
                        tvResult.text = "No meals found"
                    }
                } catch (e: Exception) {
                    tvResult.text = e.message
                }
            }
        }

        btnSearchMealName.setOnClickListener {
            val mealName = etMealName.text.toString().trim()

            if (mealName.isEmpty()) {
                tvResult.text = "Enter a meal name"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = repository.searchMealByName(mealName)
                    val meals = response.meals

                    if (!meals.isNullOrEmpty()) {
                        tvResult.text = meals.joinToString("\n\n") {
                            "${it.idMeal}\n${it.strMeal}\n${it.strCategory}\n${it.strArea}"
                        }
                    } else {
                        tvResult.text = "No meals found"
                    }

                } catch (e: Exception) {
                    tvResult.text = e.message
                }
            }
        }

        btnMealDetails.setOnClickListener {
            val mealId = etMealId.text.toString().trim()

            if (mealId.isEmpty()) {
                tvResult.text = "Enter a meal id"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = repository.getMealDetails(mealId)
                    val meal = response.meals?.firstOrNull()

                    if (meal != null) {
                        tvResult.text =
                            "Name: ${meal.strMeal}\n\n" +
                                    "Category: ${meal.strCategory}\n\n" +
                                    "Country: ${meal.strArea}\n\n" +
                                    "Instructions:\n${meal.strInstructions}\n\n" +
                                    "YouTube:\n${meal.strYoutube}"
                    } else {
                        tvResult.text = "Meal not found"
                    }

                } catch (e: Exception) {
                    tvResult.text = e.message
                }
            }
        }
    }
}
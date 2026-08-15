package com.example.foodplanner

class MealRepository {

    private val api = RetrofitClient.apiService

    suspend fun getMealOfTheDay(): MealResponse {
        return api.getMealOfTheDay()
    }

    suspend fun getCategories(): CategoryResponse {
        return api.getCategories()
    }

    suspend fun getAreas(): AreaResponse {
        return api.getAreas()
    }

    suspend fun getIngredients(): IngredientResponse {
        return api.getIngredients()
    }

    suspend fun getMealsByCategory(category: String): MealResponse {
        return api.getMealsByCategory(category)
    }

    suspend fun getMealsByArea(area: String): MealResponse {
        return api.getMealsByArea(area)
    }

    suspend fun getMealsByIngredient(ingredient: String): MealResponse {
        return api.getMealsByIngredient(ingredient)
    }

    suspend fun searchMealByName(mealName: String): MealResponse {
        return api.searchMealByName(mealName)
    }

    suspend fun getMealDetails(mealId: String): MealResponse {
        return api.getMealDetails(mealId)
    }
}
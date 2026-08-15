package com.example.foodplanner

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("random.php")
    suspend fun getMealOfTheDay(): MealResponse

    @GET("categories.php")
    suspend fun getCategories(): CategoryResponse

    @GET("list.php?a=list")
    suspend fun getAreas(): AreaResponse

    @GET("list.php?i=list")
    suspend fun getIngredients(): IngredientResponse

    @GET("filter.php")
    suspend fun getMealsByCategory(
        @Query("c") category: String
    ): MealResponse

    @GET("filter.php")
    suspend fun getMealsByArea(
        @Query("a") area: String
    ): MealResponse

    @GET("filter.php")
    suspend fun getMealsByIngredient(
        @Query("i") ingredient: String
    ): MealResponse

    @GET("search.php")
    suspend fun searchMealByName(
        @Query("s") mealName: String
    ): MealResponse

    @GET("lookup.php")
    suspend fun getMealDetails(
        @Query("i") mealId: String
    ): MealResponse
}
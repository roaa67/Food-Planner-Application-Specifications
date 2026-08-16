package com.example.foodplanner

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("random.php")
    fun getMealOfTheDay(): Single<MealResponse>

    @GET("categories.php")
    fun getCategories(): Single<CategoryResponse>

    @GET("list.php?a=list")
    fun getAreas(): Single<AreaResponse>

    @GET("list.php?i=list")
    fun getIngredients(): Single<IngredientResponse>

    @GET("filter.php")
    fun getMealsByCategory(
        @Query("c") category: String
    ): Single<MealResponse>

    @GET("filter.php")
    fun getMealsByArea(
        @Query("a") area: String
    ): Single<MealResponse>

    @GET("filter.php")
    fun getMealsByIngredient(
        @Query("i") ingredient: String
    ): Single<MealResponse>

    @GET("search.php")
    fun searchMealByName(
        @Query("s") mealName: String
    ): Single<MealResponse>

    @GET("lookup.php")
    fun getMealDetails(
        @Query("i") mealId: String
    ): Single<MealResponse>
}
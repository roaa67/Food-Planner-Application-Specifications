package com.example.foodplanner

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

class MealRepository(
    private val mealDao: MealDao
) {

    private val api = RetrofitClient.apiService


    // ==========================================
    // API - Engineer 3
    // ==========================================

    fun getMealOfTheDay(): Single<MealResponse> {
        return api.getMealOfTheDay()
    }

    fun getCategories(): Single<CategoryResponse> {
        return api.getCategories()
    }

    fun getAreas(): Single<AreaResponse> {
        return api.getAreas()
    }

    fun getIngredients(): Single<IngredientResponse> {
        return api.getIngredients()
    }

    fun getMealsByCategory(
        category: String
    ): Single<MealResponse> {
        return api.getMealsByCategory(category)
    }

    fun getMealsByArea(
        area: String
    ): Single<MealResponse> {
        return api.getMealsByArea(area)
    }

    fun getMealsByIngredient(
        ingredient: String
    ): Single<MealResponse> {
        return api.getMealsByIngredient(ingredient)
    }

    fun searchMealByName(
        mealName: String
    ): Single<MealResponse> {
        return api.searchMealByName(mealName)
    }

    fun getMealDetails(
        mealId: String
    ): Single<MealResponse> {
        return api.getMealDetails(mealId)
    }


    // ==========================================
    // FAVORITES - Engineer 2 / Room
    // ==========================================

    fun getAllFavorites(): Flowable<List<FavoriteMeal>> {
        return mealDao.getAllFavorites()
    }

    fun getFavoriteById(
        mealId: String
    ): Maybe<FavoriteMeal> {
        return mealDao.getFavoriteById(mealId)
    }

    fun isFavorite(
        mealId: String
    ): Single<Boolean> {
        return mealDao.isFavorite(mealId)
    }

    fun addFavorite(
        meal: FavoriteMeal
    ): Completable {
        return mealDao.insertFavorite(meal)
    }

    fun removeFavorite(
        meal: FavoriteMeal
    ): Completable {
        return mealDao.deleteFavorite(meal)
    }

    fun removeFavoriteById(
        mealId: String
    ): Completable {
        return mealDao.deleteFavoriteById(mealId)
    }


    // ==========================================
    // WEEKLY PLANNER - Engineer 2
    // ==========================================

    fun getAllPlannedMeals(): Flowable<List<PlannedMeal>> {
        return mealDao.getAllPlannedMeals()
    }

    fun getMealsForDay(
        day: String
    ): Flowable<List<PlannedMeal>> {
        return mealDao.getMealsForDay(day)
    }

    fun addPlannedMeal(
        meal: PlannedMeal
    ): Completable {
        return mealDao.insertPlannedMeal(meal)
    }

    fun updatePlannedMeal(
        meal: PlannedMeal
    ): Completable {
        return mealDao.updatePlannedMeal(meal)
    }

    fun deletePlannedMeal(
        meal: PlannedMeal
    ): Completable {
        return mealDao.deletePlannedMeal(meal)
    }

    fun deletePlannedMealById(
        id: Int
    ): Completable {
        return mealDao.deletePlannedMealById(id)
    }

    fun deleteMealsForDay(
        day: String
    ): Completable {
        return mealDao.deleteMealsForDay(day)
    }

    fun deleteAllPlannedMeals(): Completable {
        return mealDao.deleteAllPlannedMeals()
    }
}
package com.example.testapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MealDao {

    // =========================
    // Favorites
    // =========================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(meal: FavoriteMeal)

    @Delete
    suspend fun deleteFavorite(meal: FavoriteMeal)

    @Query("DELETE FROM favorites WHERE id = :mealId")
    suspend fun deleteFavoriteById(mealId: String)

    @Query("SELECT * FROM favorites ORDER BY name ASC")
    suspend fun getAllFavorites(): List<FavoriteMeal>

    @Query("SELECT * FROM favorites WHERE id = :mealId LIMIT 1")
    suspend fun getFavoriteById(mealId: String): FavoriteMeal?

    @Query(
        "SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :mealId)"
    )
    suspend fun isFavorite(mealId: String): Boolean


    // =========================
    // Weekly Planner
    // =========================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlannedMeal(meal: PlannedMeal)

    @Update
    suspend fun updatePlannedMeal(meal: PlannedMeal)

    @Delete
    suspend fun deletePlannedMeal(meal: PlannedMeal)

    @Query("DELETE FROM planned_meals WHERE id = :mealId")
    suspend fun deletePlannedMealById(mealId: Int)

    @Query("SELECT * FROM planned_meals ORDER BY id ASC")
    suspend fun getAllPlannedMeals(): List<PlannedMeal>

    @Query(
        "SELECT * FROM planned_meals WHERE day = :day ORDER BY id ASC"
    )
    suspend fun getMealsForDay(day: String): List<PlannedMeal>

    @Query(
        "DELETE FROM planned_meals WHERE day = :day"
    )
    suspend fun deleteMealsForDay(day: String)

    @Query("DELETE FROM planned_meals")
    suspend fun deleteAllPlannedMeals()
}
package com.example.foodplanner

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

@Dao
interface MealDao {

    // FAVORITES

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(meal: FavoriteMeal): Completable

    @Delete
    fun deleteFavorite(meal: FavoriteMeal): Completable

    @Query("DELETE FROM favorites WHERE id = :mealId")
    fun deleteFavoriteById(mealId: String): Completable

    @Query("SELECT * FROM favorites ORDER BY name ASC")
    fun getAllFavorites(): Flowable<List<FavoriteMeal>>

    @Query("SELECT * FROM favorites WHERE id = :mealId LIMIT 1")
    fun getFavoriteById(mealId: String): Maybe<FavoriteMeal>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :mealId)")
    fun isFavorite(mealId: String): Single<Boolean>


    // WEEKLY PLANNER

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlannedMeal(meal: PlannedMeal): Completable

    @Update
    fun updatePlannedMeal(meal: PlannedMeal): Completable

    @Delete
    fun deletePlannedMeal(meal: PlannedMeal): Completable

    @Query("DELETE FROM planned_meals WHERE id = :mealId")
    fun deletePlannedMealById(mealId: Int): Completable

    @Query("SELECT * FROM planned_meals ORDER BY id ASC")
    fun getAllPlannedMeals(): Flowable<List<PlannedMeal>>

    @Query("SELECT * FROM planned_meals WHERE day = :day ORDER BY id ASC")
    fun getMealsForDay(day: String): Flowable<List<PlannedMeal>>

    @Query("DELETE FROM planned_meals WHERE day = :day")
    fun deleteMealsForDay(day: String): Completable

    @Query("DELETE FROM planned_meals")
    fun deleteAllPlannedMeals(): Completable
}
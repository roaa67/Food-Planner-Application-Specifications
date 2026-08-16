package com.example.foodplanner

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "planned_meals")
data class PlannedMeal(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val mealId: String,

    val mealName: String,

    val mealImage: String,

    val day: String
)
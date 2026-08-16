package com.example.foodplanner

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteMeal(

    @PrimaryKey
    val id: String,

    val name: String,

    val image: String,

    val country: String? = null,

    val instructions: String? = null
)
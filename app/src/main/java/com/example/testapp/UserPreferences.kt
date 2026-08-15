package com.example.testapp

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )

    // =========================
    // Login
    // =========================

    fun setLoggedIn(isLoggedIn: Boolean) {
        preferences.edit()
            .putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
            .apply()
    }

    fun isLoggedIn(): Boolean {
        return preferences.getBoolean(
            KEY_IS_LOGGED_IN,
            false
        )
    }


    // =========================
    // Guest
    // =========================

    fun setGuest(isGuest: Boolean) {
        preferences.edit()
            .putBoolean(KEY_IS_GUEST, isGuest)
            .apply()
    }

    fun isGuest(): Boolean {
        return preferences.getBoolean(
            KEY_IS_GUEST,
            false
        )
    }


    // =========================
    // User ID
    // =========================

    fun saveUserId(userId: String) {
        preferences.edit()
            .putString(KEY_USER_ID, userId)
            .apply()
    }

    fun getUserId(): String? {
        return preferences.getString(
            KEY_USER_ID,
            null
        )
    }


    // =========================
    // Clear Session
    // =========================

    fun clearSession() {
        preferences.edit()
            .remove(KEY_IS_LOGGED_IN)
            .remove(KEY_IS_GUEST)
            .remove(KEY_USER_ID)
            .apply()
    }

    companion object {

        private const val PREF_NAME = "food_planner_preferences"

        private const val KEY_IS_LOGGED_IN = "is_logged_in"

        private const val KEY_IS_GUEST = "is_guest"

        private const val KEY_USER_ID = "user_id"
    }
}
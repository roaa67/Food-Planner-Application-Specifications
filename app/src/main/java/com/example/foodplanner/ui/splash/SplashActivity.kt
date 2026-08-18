package com.example.foodplanner.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.foodplanner.MainActivity
import com.example.foodplanner.R
import com.example.foodplanner.ui.auth.LoginActivity

/**
 * SplashActivity – Entry point of the Food Planner app.
 *
 * Course reference:
 *   - Activity Lifecycle (p58-73): extends AppCompatActivity, onCreate()
 *   - Glide image/GIF loading (p633-637): loads animated GIF logo into ImageView
 *   - SharedPreferences (p504-519): checks user session state
 *   - Explicit Intent (p243-245): navigates to LoginActivity or MainActivity
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    companion object {
        private const val SPLASH_DELAY_MS = 3000L  // 3 seconds
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Load animated GIF logo using Glide (Course p633-637)
        val ivSplashLogo = findViewById<ImageView>(R.id.iv_splash_logo)
        if (ivSplashLogo != null) {
            Glide.with(this)
                .asGif()
                .load(R.drawable.splash_logo_animated)
                .into(ivSplashLogo)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            checkAuthAndNavigate()
        }, SPLASH_DELAY_MS)
    }

    private fun checkAuthAndNavigate() {
        val sharedPrefs = getSharedPreferences("food_planner_prefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPrefs.getBoolean("is_logged_in", false)

        val nextActivity = if (isLoggedIn) {
            MainActivity::class.java
        } else {
            LoginActivity::class.java
        }

        val intent = Intent(this, nextActivity)
        startActivity(intent)
        finish()
    }
}

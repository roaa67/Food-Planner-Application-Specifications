package com.example.foodplanner.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.foodplanner.MainActivity
import com.example.foodplanner.R

/**
 * SplashActivity – Entry point of the Food Planner app.
 *
 * Course reference: Activity Lifecycle (p58-73)
 *   - Extends AppCompatActivity (course p53: "class MainActivity : AppCompatActivity()")
 *   - Uses onCreate() lifecycle callback (course p67)
 *   - Navigates to MainActivity using explicit Intent (course p243-245)
 *
 * Project spec: "The application shows a splash screen with animation (Lottie)"
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    companion object {
        private const val SPLASH_DELAY_MS = 3000L  // 3 seconds
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Connect activity with its layout (course p54 – setContentView)
        setContentView(R.layout.activity_splash)

        // Navigate to MainActivity after SPLASH_DELAY_MS (3 seconds)
        // Using Handler + Looper as per Android best practices (course p56)
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToMain()
        }, SPLASH_DELAY_MS)
    }

    /**
     * Explicit Intent to start MainActivity (course p243-245)
     * "Explicit intent: explicitly specify the component to start by its class name"
     */
    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        // Finish splash so user can't go back to it (course p268 - back stack)
        finish()
    }
}

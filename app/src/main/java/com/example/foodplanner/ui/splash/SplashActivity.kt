package com.example.foodplanner.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.example.foodplanner.MainActivity
import com.example.foodplanner.R
import com.example.foodplanner.ui.auth.LoginActivity
import com.google.android.material.snackbar.Snackbar

/**
 * SplashActivity – Entry point of the Food Planner app.
 *
 * Course reference:
 *   - Activity Lifecycle (p58-73): extends AppCompatActivity, onCreate()
 *   - Lottie Animation & Connectivity (Course p608 Permissions, p560 Snackbar)
 *   - Network State Check: Happy Chef Lottie when online, Sad/Offline Chef Lottie when offline
 *   - SharedPreferences (p504-519): checks user session state
 *   - Explicit Intent (p243-245): navigates to LoginActivity or MainActivity
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    companion object {
        private const val SPLASH_DELAY_MS = 3200L  // 3.2 seconds
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val lottieLogo = findViewById<LottieAnimationView>(R.id.lottie_splash_logo)
        val tvOfflineStatus = findViewById<TextView>(R.id.tv_offline_status)

        // Check if device has active internet connection (Course p608)
        val isOnline = isNetworkAvailable()

        // Save network state in SharedPreferences for offline mode handling (Course p511)
        val sharedPrefs = getSharedPreferences("food_planner_prefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean("is_online", isOnline).apply()

        if (isOnline) {
            // Happy chef animation when online
            lottieLogo?.setAnimation(R.raw.chef_logo_happy)
            tvOfflineStatus?.visibility = View.GONE
        } else {
            // Sad chef animation when offline (User requirement)
            lottieLogo?.setAnimation(R.raw.chef_logo_offline)
            tvOfflineStatus?.visibility = View.VISIBLE
            Snackbar.make(findViewById(R.id.splash_root), R.string.no_internet, Snackbar.LENGTH_LONG).show()
        }

        lottieLogo?.playAnimation()

        Handler(Looper.getMainLooper()).postDelayed({
            checkAuthAndNavigate()
        }, SPLASH_DELAY_MS)
    }

    /**
     * Checks network availability using ConnectivityManager (Course p608)
     */
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
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

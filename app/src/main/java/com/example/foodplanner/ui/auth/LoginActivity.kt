package com.example.foodplanner.ui.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.foodplanner.MainActivity
import com.example.foodplanner.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

/**
 * LoginActivity – Manages user authentication and guest access.
 *
 * Course references:
 *   - Activity Lifecycle (p58-73): extends AppCompatActivity, implements onCreate()
 *   - Event Handling (p232-237): sets View.OnClickListener SAM lambdas
 *   - Explicit Intents (p243-245): Intent(this, TargetActivity::class.java)
 *   - SharedPreferences (p504-519): saves user login state and guest session
 *   - Material Components & Snackbar (p560-561): visual feedback
 *
 * Project Specs:
 *   - Email/Password login
 *   - Social Authentication: Google, Facebook, Apple
 *   - Guest Mode: "Users can choose to be guests..."
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: MaterialButton
    private lateinit var btnGoogle: ImageButton
    private lateinit var btnFacebook: ImageButton
    private lateinit var btnApple: ImageButton
    private lateinit var tvGuestMode: TextView
    private lateinit var tvGoToSignUp: TextView
    private lateinit var tvForgotPassword: TextView
    private lateinit var btnBack: ImageView

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize SharedPreferences (Course p508-509)
        sharedPreferences = getSharedPreferences("food_planner_prefs", Context.MODE_PRIVATE)

        // Check if user is already logged in (Course p517-518)
        if (isUserLoggedIn()) {
            navigateToMain()
            return
        }

        initViews()
        setupListeners()
    }

    private fun initViews() {
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        btnGoogle = findViewById(R.id.btn_google)
        btnFacebook = findViewById(R.id.btn_facebook)
        btnApple = findViewById(R.id.btn_apple)
        tvGuestMode = findViewById(R.id.tv_guest_mode)
        tvGoToSignUp = findViewById(R.id.tv_go_to_signup)
        tvForgotPassword = findViewById(R.id.tv_forgot_password)
        btnBack = findViewById(R.id.btn_back)
    }

    private fun setupListeners() {
        // Back Button
        btnBack.setOnClickListener {
            finish()
        }

        // Email & Password Login
        btnLogin.setOnClickListener {
            handleEmailLogin()
        }

        // Social Sign-in Buttons
        btnGoogle.setOnClickListener {
            handleSocialLogin("Google")
        }

        btnFacebook.setOnClickListener {
            handleSocialLogin("Facebook")
        }

        btnApple.setOnClickListener {
            handleSocialLogin("Apple")
        }

        // Guest Mode (Project Specs)
        tvGuestMode.setOnClickListener {
            saveSession(isGuest = true, email = "guest@foodplanner.app", name = "Guest User")
            showFeedback(getString(R.string.guest_mode))
            navigateToMain()
        }

        // Go to Sign Up (Course p243 - Explicit Intent)
        tvGoToSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Forgot Password
        tvForgotPassword.setOnClickListener {
            showFeedback("Password reset link sent to your email")
        }
    }

    private fun handleEmailLogin() {
        val email = etEmail.text?.toString()?.trim() ?: ""
        val password = etPassword.text?.toString()?.trim() ?: ""

        if (email.isEmpty() || password.isEmpty()) {
            showFeedback(getString(R.string.error_empty_fields))
            return
        }

        // Save session in SharedPreferences (Course p511-515)
        saveSession(isGuest = false, email = email, name = email.substringBefore("@"))
        showFeedback(getString(R.string.login_success))
        navigateToMain()
    }

    private fun handleSocialLogin(provider: String) {
        val message = getString(R.string.social_login_msg, provider)
        showFeedback(message)

        // Save authenticated state with provider info (Course p511-515)
        saveSession(isGuest = false, email = "user@$provider.com", name = "$provider User")
        
        // Navigate after brief delay / callback
        btnLogin.postDelayed({
            navigateToMain()
        }, 800)
    }

    /**
     * Save user session in SharedPreferences (Course p511-516)
     * "sharedPreferences.edit().putString(...).apply()"
     */
    private fun saveSession(isGuest: Boolean, email: String, name: String) {
        sharedPreferences.edit().apply {
            putBoolean("is_logged_in", true)
            putBoolean("is_guest", isGuest)
            putString("user_email", email)
            putString("user_name", name)
            apply()
        }
    }

    private fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        // Clear back stack so user can't return to login after logging in (Course p267-272)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showFeedback(message: String) {
        Snackbar.make(findViewById(R.id.login_root), message, Snackbar.LENGTH_SHORT).show()
    }
}

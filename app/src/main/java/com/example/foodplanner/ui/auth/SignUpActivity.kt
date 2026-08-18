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
 * SignUpActivity – Handles new user registration.
 *
 * Course references:
 *   - Activity Lifecycle (p58-73): extends AppCompatActivity, onCreate()
 *   - Event Handling (p232-237): click listeners
 *   - Explicit Intents (p243-245): navigation between activities
 *   - SharedPreferences (p504-519): local persistence of account credentials
 *   - Snackbar (p560-561): alerts and user feedback
 */
class SignUpActivity : AppCompatActivity() {

    private lateinit var etName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var btnSignUp: MaterialButton
    private lateinit var btnGoogle: ImageButton
    private lateinit var btnFacebook: ImageButton
    private lateinit var btnApple: ImageButton
    private lateinit var tvGoToLogin: TextView
    private lateinit var btnBack: ImageView

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        sharedPreferences = getSharedPreferences("food_planner_prefs", Context.MODE_PRIVATE)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        etName = findViewById(R.id.et_name)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        etConfirmPassword = findViewById(R.id.et_confirm_password)
        btnSignUp = findViewById(R.id.btn_signup)
        btnGoogle = findViewById(R.id.btn_google)
        btnFacebook = findViewById(R.id.btn_facebook)
        btnApple = findViewById(R.id.btn_apple)
        tvGoToLogin = findViewById(R.id.tv_go_to_login)
        btnBack = findViewById(R.id.btn_back)
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        btnSignUp.setOnClickListener {
            handleSignUp()
        }

        btnGoogle.setOnClickListener {
            handleSocialSignUp("Google")
        }

        btnFacebook.setOnClickListener {
            handleSocialSignUp("Facebook")
        }

        btnApple.setOnClickListener {
            handleSocialSignUp("Apple")
        }

        tvGoToLogin.setOnClickListener {
            finish()
        }
    }

    private fun handleSignUp() {
        val name = etName.text?.toString()?.trim() ?: ""
        val email = etEmail.text?.toString()?.trim() ?: ""
        val password = etPassword.text?.toString()?.trim() ?: ""
        val confirmPassword = etConfirmPassword.text?.toString()?.trim() ?: ""

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showFeedback(getString(R.string.error_empty_fields))
            return
        }

        if (password != confirmPassword) {
            showFeedback(getString(R.string.error_password_match))
            return
        }

        // Save new user profile in SharedPreferences (Course p511-516)
        sharedPreferences.edit().apply {
            putBoolean("is_logged_in", true)
            putBoolean("is_guest", false)
            putString("user_name", name)
            putString("user_email", email)
            apply()
        }

        showFeedback(getString(R.string.sign_up_success))

        // Navigate to MainActivity
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun handleSocialSignUp(provider: String) {
        val message = getString(R.string.social_login_msg, provider)
        showFeedback(message)

        sharedPreferences.edit().apply {
            putBoolean("is_logged_in", true)
            putBoolean("is_guest", false)
            putString("user_name", "$provider User")
            putString("user_email", "user@$provider.com")
            apply()
        }

        btnSignUp.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }, 800)
    }

    private fun showFeedback(message: String) {
        Snackbar.make(findViewById(R.id.signup_root), message, Snackbar.LENGTH_SHORT).show()
    }
}

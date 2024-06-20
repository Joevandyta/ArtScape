package com.jovan.artscape.ui.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.jovan.artscape.R
import com.jovan.artscape.ViewModelFactory
import com.jovan.artscape.remote.response.ApiResponse
import com.jovan.artscape.ui.login.LoginActivity
import com.jovan.artscape.ui.main.MainViewModel
import com.jovan.artscape.utils.DialogUtils
import com.jovan.artscape.utils.NetworkUtils
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        if (NetworkUtils.isNetworkAvailable(this)) {
            auth = Firebase.auth
            val motionLayout = findViewById<MotionLayout>(R.id.motionLayout)
            motionLayout.addTransitionListener(
                object : MotionLayout.TransitionListener {
                    override fun onTransitionStarted(
                        p0: MotionLayout?,
                        p1: Int,
                        p2: Int,
                    ) {
                    }

                    override fun onTransitionChange(
                        p0: MotionLayout?,
                        p1: Int,
                        p2: Int,
                        p3: Float,
                    ) {
                    }

                    override fun onTransitionCompleted(
                        p0: MotionLayout?,
                        p1: Int,
                    ) {
                        getSession(auth)
                    }

                    override fun onTransitionTrigger(
                        p0: MotionLayout?,
                        p1: Int,
                        p2: Boolean,
                        p3: Float,
                    ) {
                    }
                },
            )
        } else {
            Log.d("ERROR", "Network Not Available")
            DialogUtils.showNetworkSettingsDialog(this)
        }
    }

    private fun getSession(auth: FirebaseAuth) {
        val firebaseUser = auth.currentUser

        viewModel.getSession().observe(this) { user ->

            if (firebaseUser == null || !user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Log.d("ID", "Token: ${user.uid}")
                Log.d("TOKEN", "Token: ${user.token}")

                viewModel.setUserData(user.uid)
                viewModel.getUserData().observe(this) {
                    when (it) {
                        is ApiResponse.Success -> {
                            Log.d("CURENT USER", "user Data: ${it.data}")
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }

                        is ApiResponse.Error -> {
                            if (it.error.contains("User not found")) {
                                signOut()
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun signOut() {
        lifecycleScope.launch {
            viewModel.logout()
            val credentialManager = CredentialManager.create(this@SplashActivity)
            auth.signOut()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        }
    }
}

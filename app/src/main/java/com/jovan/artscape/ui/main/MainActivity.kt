package com.jovan.artscape.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.jovan.artscape.R
import com.jovan.artscape.ViewModelFactory
import com.jovan.artscape.databinding.ActivityMainBinding
import com.jovan.artscape.remote.response.ApiResponse
import com.jovan.artscape.ui.login.LoginActivity
import com.jovan.artscape.ui.main.account.AccountFragment
import com.jovan.artscape.ui.main.home.HomeFragment
import com.jovan.artscape.ui.upload.UploadActivity
import com.jovan.artscape.ui.upload.UploadActivity.Companion.EXTRA_IMAGE
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentImageUri: Uri? = null
    private lateinit var auth: FirebaseAuth
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val launcherGallery =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia(),
        ) { uri: Uri? ->
            if (uri != null) {
                currentImageUri = uri
                val intent = Intent(this, UploadActivity::class.java)
                intent.putExtra(EXTRA_IMAGE, currentImageUri.toString())
                startActivity(intent)
            } else {
                Log.d("Photo Picker", "No media selected")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        getSession(auth)

        setContentView(binding.root)

        bottomAppBar()
        // Set initial fragment
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }
    }

    private fun getSession(auth: FirebaseAuth) {
        val firebaseUser = auth.currentUser
        viewModel.getSession().observe(this) { user ->
            if (firebaseUser == null || !user.isLogin) {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            } else {
                Log.d("ID", "Token: ${user.uid}")
                Log.d("TOKEN", "Token: ${user.token}")
            }
            viewModel.setUserData(user.uid)
            viewModel.getUserData().observe(this) {
                when (it) {
                    is ApiResponse.Success -> {
                        Log.d("CURENT USER", "user Data: ${it.data}")
                    }
                    is ApiResponse.Error -> {
                        if (it.error.contains("User not found")) {
                            signOut()
                        } else {
                            Log.d("ERROR", "An error occurred: ${it.error}")
                        }
                    }
                }
            }
        }
    }

    private fun bottomAppBar() {
        binding.bottomAppBar.background = null
        binding.bottomAppBar.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.account -> {
                    replaceFragment(AccountFragment())
                    true
                }

                else -> false
            }
        }

        binding.btnUploadArt.setOnClickListener {
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun signOut() {
        lifecycleScope.launch {
            viewModel.logout()
            val credentialManager = CredentialManager.create(this@MainActivity)
            auth.signOut()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}

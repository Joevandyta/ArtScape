package com.jovan.artscape.ui.login

import android.content.Intent
import android.credentials.GetCredentialException
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.jovan.artscape.R
import com.jovan.artscape.ViewModelFactory
import com.jovan.artscape.data.pref.UserModel
import com.jovan.artscape.databinding.ActivityLoginBinding
import com.jovan.artscape.remote.response.ApiResponse
import com.jovan.artscape.ui.login.artist.UserDataActivity
import com.jovan.artscape.ui.login.artist.UserDataActivity.Companion.EXTRA_ID_TOKEN
import com.jovan.artscape.ui.main.MainActivity
import com.jovan.artscape.utils.DialogUtils
import com.jovan.artscape.utils.NetworkUtils
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (NetworkUtils.isNetworkAvailable(this)) {
            auth = Firebase.auth
            onStartLogin()
            actionButton()
        } else {
            showLoading(false)
            Log.d("ERROR", "Network Not Available")
            DialogUtils.showNetworkSettingsDialog(this)
        }
    }

    private fun onStartLogin() {
        showLoading(true)
        val currentUser = auth.currentUser

        viewModel.getSession().observe(this@LoginActivity) {
            if (currentUser != null && it.isLogin) {
                updateUI(currentUser)
            } else if (currentUser != null) {
                currentUser.getIdToken(true).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uidToken = task.result?.token
                        saveToDatabase(currentUser, uidToken.toString())
                        showLoading(false)
                        Log.d("firebaseAuthWithGoogle TOKEN", uidToken.toString())
                    } else {
                        showLoading(false)
                        // Handle error -> task.getException()
                        Log.d("ERROR", "Error getting ID Token: ${task.exception}")
                    }
                }
            } else {
                showLoading(false)
                Log.d("ERROR", "User Must Login")
            }
        }
    }

    private fun signIn() {
        val credentialManager =
            CredentialManager.create(this) // import from androidx.CredentialManager
        val googleIdOption =
            GetGoogleIdOption
                .Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(getString(R.string.web_client_id))
                .build()
        val request =
            GetCredentialRequest
                .Builder() // import from androidx.CredentialManager
                .addCredentialOption(googleIdOption)
                .build()

        lifecycleScope.launch {
            showLoading(true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                try {
                    val result: GetCredentialResponse =
                        credentialManager.getCredential(
                            request = request,
                            context = this@LoginActivity,
                        )
                    handleSignIn(result)
                } catch (e: NoCredentialException) {
                    Log.d("error", "No credentials available: ${e.message}")
                    showToast("No credentials available. Please sign in manually.")
                    showLoading(false)
                } catch (e: GetCredentialException) {
                    Log.d("error", e.message.toString())
                    showToast("Failed to get credentials: ${e.message}")
                    showLoading(false)
                }
            } else {
                try {
                    val result: GetCredentialResponse =
                        credentialManager.getCredential(
                            request = request,
                            context = this@LoginActivity,
                        )
                    handleSignIn(result)
                } catch (e: NoCredentialException) {
                    Log.d("error NoCredentialException", "No credentials available: ${e.message}")
                    showToast("No credentials available. Please sign in manually.")
                    showLoading(false)
                } catch (e: Exception) {
                    showLoading(false)

                    Log.d("signIn error exeption", e.message.toString())
                }
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    // Process Login dengan Firebase Auth
                    try {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                        Log.e("TOKEN HANDLE SIGN IN", googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                }
            }

            else -> {
                showLoading(false)
                // Catch any unrecognized credential type here.
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        Log.d("TOKEN", credential.toString())

        auth
            .signInWithCredential(credential)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    val user = auth.currentUser
                    user?.getIdToken(true)?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val uidToken = task.result?.token
                            saveToDatabase(user, uidToken.toString())
                            Log.d("firebaseAuthWithGoogle TOKEN", uidToken.toString())
                        } else {
                            showLoading(false)
                            // Handle error -> task.getException()
                            Log.d("ERROR", "Error getting ID Token: ${task.exception}")
                        }
                    }
                    Log.d(TAG, "signInWithCredential:success")
                } else {
                    Log.d(TAG, "signInWithCredential:failure", it.exception)
                    showLoading(false)
                    updateUI(null)
                }
            }
    }

    private fun saveToDatabase(
        user: FirebaseUser?,
        idToken: String,
    ) {
        viewModel.setLogin(idToken)
        viewModel.getLogin().observe(this@LoginActivity) {
            when (it) {
                is ApiResponse.Success -> {
                    // Show ID in Toast
                    Log.d("UserDataActivity", "User ID: ${it.data.uid}")
                    showLoading(false)
                    MaterialAlertDialogBuilder(this).apply {
                        Log.d(
                            "Save Genre AlertDialog",
                            "${user?.uid}",
                        )
                        setTitle("Yeah")
                        setMessage(it.data.message)
                        setPositiveButton("Continue") { _, _ ->
                            viewModel.saveSession(UserModel(it.data.uid, idToken))
                            updateUI(user)
                        }
                        setCancelable(false)
                        create()
                        show()
                    }
                }

                is ApiResponse.Error -> {
                    showLoading(false)
                    // Show error message in Toast
                    if (it.error.contains("Additional data required")) {
                        val intent = Intent(this@LoginActivity, UserDataActivity::class.java)
                        intent.putExtra(EXTRA_ID_TOKEN, idToken)
                        startActivity(intent)
                        Log.d("UserDataActivity", "Error: ${it.error}")
                    } else {
                        Log.d("UserDataActivity", "Error: Unexpected")
                    }
                    finish()
                }
            }
        }
    }

    private fun updateUI(curentUser: FirebaseUser?) {
        if (curentUser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun actionButton() {
        binding.signInButton.setOnClickListener {
            signIn()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}

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
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
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
import com.jovan.artscape.remote.response.UserResponse
import com.jovan.artscape.ui.login.artist.UserDataActivity
import com.jovan.artscape.ui.login.artist.UserDataActivity.Companion.EXTRA_ID_TOKEN
import com.jovan.artscape.ui.main.MainActivity
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

            auth = Firebase.auth
            actionButton()
    }

    private fun signIn() {
        val credentialManager =
            CredentialManager.create(this) //import from androidx.CredentialManager
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.web_client_id))
            .build()
        val request = GetCredentialRequest.Builder() //import from androidx.CredentialManager
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            showLoading(true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                try {
                    val result: GetCredentialResponse = credentialManager.getCredential(
                        request = request,
                        context = this@LoginActivity
                    )
                    handleSignIn(result)
                } catch (e: GetCredentialException) {
                    Log.d("error", e.message.toString())
                }
            } else {
                try {
                    val result: GetCredentialResponse = credentialManager.getCredential(
                        request = request,
                        context = this@LoginActivity
                    )
                    handleSignIn(result)
                } catch (e: Exception) {
                    Log.d("error", e.message.toString())
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

        auth.signInWithCredential(credential)
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

    private fun saveToDatabase(user: FirebaseUser?, idToken: String) {

        viewModel.setLogin(idToken)
        viewModel.getLogin().observe(this@LoginActivity) {
            when (it) {
                is UserResponse.Success -> {
                    // Show ID in Toast
                    showToast("User ID: ${it.data.uid}")
                    Log.d("UserDataActivity", "User ID: ${it.data.uid}")
                    viewModel.saveSession(UserModel(it.data.uid, idToken)).apply {
                        showLoading(false)
                        updateUI(user)
                    }
                }

                is UserResponse.Error -> {
                    showLoading(false)
                    // Show error message in Toast
                    if (it.error.contains("Additional data required")) {
                        val intent = Intent(this@LoginActivity, UserDataActivity::class.java)
                        intent.putExtra(EXTRA_ID_TOKEN, idToken)
                        startActivity(intent)
                        showToast("Error: ${it.error}")
                        Log.d("UserDataActivity", "Error: ${it.error}")
                    }else{
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

    override fun onStart() {
        super.onStart()
        showLoading(true)
        viewModel.getSession().observe(this@LoginActivity) {
            val currentUser = auth.currentUser
            if (it.isLogin && currentUser != null) {
                updateUI(currentUser)
            } else currentUser?.getIdToken(true)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uidToken = task.result?.token
                    saveToDatabase(currentUser, uidToken.toString())
                    Log.d("firebaseAuthWithGoogle TOKEN", uidToken.toString())
                } else {
                    showLoading(false)
                    // Handle error -> task.getException()
                    Log.d("ERROR", "Error getting ID Token: ${task.exception}")
                }
            }
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}
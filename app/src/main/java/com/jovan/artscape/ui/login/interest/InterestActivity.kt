package com.jovan.artscape.ui.login.interest

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jovan.artscape.R
import com.jovan.artscape.ViewModelFactory
import com.jovan.artscape.data.pref.UserModel
import com.jovan.artscape.databinding.ActivityInterestBinding
import com.jovan.artscape.remote.request.AddUserRequest
import com.jovan.artscape.remote.response.ApiResponse
import com.jovan.artscape.ui.main.MainActivity

class InterestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInterestBinding
    private val viewModel by viewModels<InterestViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var selectedGenresList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInterestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar_genre)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        actionButton()
    }

    private fun addUser(addUserRequest: AddUserRequest?) {
        if (addUserRequest != null) {
            viewModel.addUser(addUserRequest)

            viewModel.getAddUser().observe(this) { userResponse ->
                when (userResponse) {
                    is ApiResponse.Success -> {
                        // Show ID in Toast
                        showLoading(false)
                        showToast("User ID: ${userResponse.data.uid}")
                        viewModel.saveSession(UserModel(userResponse.data.uid, addUserRequest.idToken))
                        Log.d("UserDataActivity", "User ID: ${userResponse.data.uid}")
                        MaterialAlertDialogBuilder(this@InterestActivity).apply {
                            Log.d(
                                "Save Genre AlertDialog",
                                "${addUserRequest.idToken}, ${addUserRequest.name}, ${addUserRequest.address}, ${addUserRequest.bio}, ${addUserRequest.interest}",
                            )
                            setTitle("Yeah")
                            setMessage(userResponse.data.message)
                            setPositiveButton("Continue") { _, _ ->
                                startActivity(Intent(this@InterestActivity, MainActivity::class.java))
                            }
                            setCancelable(false)
                            create()
                            show()
                        }
                    }

                    is ApiResponse.Error -> {
                        showLoading(false)
                        // Show error message in Toast
                        showToast("Error: ${userResponse.error}")
                        Log.e("UserDataActivity", "Error: ${userResponse.error}")
                        MaterialAlertDialogBuilder(this@InterestActivity).apply {
                            setTitle("Error")
                            setMessage("Cant save your user data, please try again!")
                            setPositiveButton("Continue") { _, _ ->
                            }
                            setCancelable(false)
                            create()
                            show()
                        }
                    }
                }
            }
        }
    }

    private fun actionButton() {
        binding.apply {
            chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                if (checkedIds.isEmpty()) {
                    tv.text = "No genre selected"
                    selectedGenresList = mutableListOf()
                } else {
                    selectedGenresList = mutableListOf()
                    checkedIds.forEach { id ->
                        val chip: Chip = findViewById(id)
                        selectedGenresList.add(chip.text.toString())
                    }
                    tv.text = "Selected genres: $selectedGenresList"
                }

                if (selectedGenresList.size < 3) {
                    buttonSave.isEnabled = false
                } else {
                    buttonSave.isEnabled = true
                }
            }

            buttonSave.setOnClickListener {
                showLoading(true)
                val user =
                    if (Build.VERSION.SDK_INT >= 33) {
                        intent.getParcelableExtra(EXTRA_USER_WITH_ADDRESS, AddUserRequest::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        intent.getParcelableExtra(EXTRA_USER_WITH_ADDRESS)
                    }
                user?.interest = selectedGenresList
                addUser(user)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_USER_WITH_ADDRESS = "extra_user_with_address"
    }
}

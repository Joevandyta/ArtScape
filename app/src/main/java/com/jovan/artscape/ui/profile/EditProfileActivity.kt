package com.jovan.artscape.ui.profile

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jovan.artscape.R
import com.jovan.artscape.ViewModelFactory
import com.jovan.artscape.databinding.ActivityEditProfileBinding
import com.jovan.artscape.remote.request.UpdateUserRequest
import com.jovan.artscape.remote.response.ApiResponse
import com.jovan.artscape.ui.main.MainActivity
import com.airbnb.lottie.LottieDrawable
import com.airbnb.lottie.LottieCompositionFactory
import com.jovan.artscape.R

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel by viewModels<EditProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userId = intent.getStringExtra("USER_ID") ?: ""

        binding.buttonUpdate.setOnClickListener {
            updateUserProfile()
        }
        setMyButtonEnable()
        enableButton()
        getUserResponse()

        LottieCompositionFactory.fromRawRes(this, R.raw.settings).addListener { composition ->
            binding.settingView.setComposition(composition)
            binding.settingView.repeatCount = LottieDrawable.INFINITE
            binding.settingView.playAnimation()
        }

    }

    private fun updateUserProfile() {
        val username = binding.edUsername.text.toString()
        val phoneNumber = binding.edPhoneNumber.text.toString()
        val bio = binding.edBio.text.toString()
        val updateUserRequest =
            UpdateUserRequest(
                name = username,
                phoneNumber = getString(R.string.template_62, phoneNumber),
                bio = bio,
            )

        viewModel.getSession().observe(this) {
            viewModel.editUser(it.uid, updateUserRequest)
            showLoading(true) // Show loading indicator when the update request starts
        }
    }

    private fun enableButton() {
        binding.apply {
            edUsername.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int,
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int,
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        setMyButtonEnable()
                    }
                },
            )
            edBio.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int,
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int,
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        setMyButtonEnable()
                    }
                },
            )

            edPhoneNumber.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int,
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int,
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        setMyButtonEnable()
                    }
                },
            )
        }
    }

    fun setMyButtonEnable() {
        binding.apply {
            val phoneNumber = edPhoneNumber.text.toString()

            if (phoneNumber.startsWith("0")) {
                edPhoneNumber.error = "Invalid Phone Number"
            } else {
                edPhoneNumber.error = null
            }
            val isNameValid = edUsername.text.toString().isNotEmpty()
            val isPhoneNumberValid = edPhoneNumber.text.toString().isNotEmpty() && edPhoneNumber.error.isNullOrEmpty()
            val isBioValid = edBio.text.toString().isNotEmpty()

            buttonUpdate.isEnabled = isNameValid && isBioValid && isPhoneNumberValid
        }
    }

    private fun getUserResponse() {
        viewModel.getUserResponse().observe(this) { response ->
            when (response) {
                is ApiResponse.Success -> {
                    Log.d("Edit Profile SUCCESS", "${response.data.uid} ${response.data.message}")
                    showLoading(false)
                    MaterialAlertDialogBuilder(this).apply {
                        setTitle("Success")
                        setMessage("User Data Successfully Changed")
                        setPositiveButton("Continue") { _, _ ->
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        setCancelable(false)
                        create()
                        show()
                    }
                }
                is ApiResponse.Error -> {
                    showLoading(false)
                    Log.d("Edit Profile FAILED", "${response.error} ${response.details}")
                    MaterialAlertDialogBuilder(this).apply {
                        setTitle("Failed")
                        setMessage("Unable to Change Data")
                        setPositiveButton("Continue") { _, _ -> }
                        setCancelable(false)
                        create()
                        show()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}

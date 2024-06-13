package com.jovan.artscape.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jovan.artscape.ViewModelFactory
import com.jovan.artscape.databinding.ActivityEditProfileBinding
import com.jovan.artscape.remote.request.UpdateUserRequest
import com.jovan.artscape.remote.response.ApiResponse
import com.jovan.artscape.ui.main.MainActivity

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel by viewModels<EditProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    //    private var currentImageUri: Uri? = null
    private lateinit var userId: String

    //    private val launcherGallery = registerForActivityResult(
//        ActivityResultContracts.PickVisualMedia()
//    ) { uri: Uri? ->
//        if (uri != null) {
//            currentImageUri = uri
//            binding.imgPicture.setImageURI(uri)
//            showImage()
//        } else {
//            Log.d("Photo Picker", "No media selected")
//        }
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userId = intent.getStringExtra("USER_ID") ?: ""

// //        binding.editProfileImage.setOnClickListener{
// //            startGallery()
//        }
        binding.buttonUpdate.setOnClickListener {
            updateUserProfile()
        }
        getUserResponse()
//        actionSetup()
    }

    //    private fun startGallery() {
//        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//    }
    private fun updateUserProfile() {
        val username = binding.edUsername.text.toString()
        val phoneNumber = binding.edPhoneNumber.text.toString()
        val bio = binding.edBio.text.toString()
        val updateUserRequest =
            UpdateUserRequest(
                name = username,
                phoneNumber = phoneNumber,
                bio = bio,
            )

        viewModel.getSession().observe(this) {
            viewModel.editUser(it.uid, updateUserRequest)
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
                        setMessage("User Data Successfully Change")
                        setPositiveButton("Continue") { _, _ ->
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
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
                        setMessage("Unable to change Data")
                        setPositiveButton("Continue") { _, _ ->
                        }
                        setCancelable(false)
                        create()
                        show()
                    }
                }
            }

            // Handle error response
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
//    private fun showImage() {
//        currentImageUri?.let { uri ->
//            binding.imgPicture.setImageURI(uri)
//        }
//    }
}

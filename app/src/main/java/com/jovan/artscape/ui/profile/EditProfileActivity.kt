package com.jovan.artscape.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jovan.artscape.ViewModelFactory
import com.jovan.artscape.databinding.ActivityEditProfileBinding
import com.jovan.artscape.remote.request.AddUserRequest
import com.jovan.artscape.remote.response.user.UserResponseSuccess

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel by viewModels<EditProfileViewModel>{
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

////        binding.editProfileImage.setOnClickListener{
////            startGallery()
//        }
        binding.buttonUpdate.setOnClickListener{
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
        val addUserRequest = AddUserRequest(
            idToken = "user_id_token",
            name = username,
            address = "user_address",
            bio = "user_bio",
            interest = listOf("interest1", "interest2"),
            phoneNumber = phoneNumber
        )
        viewModel.editUser(userId, addUserRequest)
    }

    private fun getUserResponse() {
        viewModel.getUserResponse().observe(this) { response ->
            if (response.isSuccessful) {
                val userData = response.body()
                userData?.let {
                    val userResponse = UserResponseSuccess(
                        message = "Profile updated successfully",
                        uid = it.uid,
                        name = it.name,
                        phoneNumber = it.phoneNumber
                    )
                    val resultIntent = Intent()
                    resultIntent.putExtra("USER_DATA", userResponse)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
            } else {
                // Handle error response
                setResult(Activity.RESULT_CANCELED)
                finish()
                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
        }
    }
//    private fun showImage() {
//        currentImageUri?.let { uri ->
//            binding.imgPicture.setImageURI(uri)
//        }
//    }
}

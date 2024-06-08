package com.jovan.artscape.ui.login.interest

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.jovan.artscape.R
import com.jovan.artscape.ViewModelFactory
import com.jovan.artscape.databinding.ActivitySelectGenreBinding
import com.jovan.artscape.remote.request.AddUserRequest
import com.jovan.artscape.remote.response.user.UserResponse
import com.jovan.artscape.ui.main.MainActivity

class SelectGenreActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectGenreBinding
    private val viewModel by viewModels<GenreViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectGenreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar_genre)
        setSupportActionBar(toolbar)

        val chipGroup: ChipGroup = findViewById(R.id.chipGroup)
        val textView: TextView = findViewById(R.id.tv)

        actionButton()

        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isEmpty()) {
                textView.text = "No genre selected"
            } else {
                val selectedGenres = checkedIds.joinToString(", ") { id ->
                    val chip: Chip = findViewById(id)
                    chip.text
                }
                textView.text = "Selected genres: $selectedGenres"
            }
        }

    }
    private fun addUser(){
        val name = "Tio"
        val email = "Tio@gmail.com"
        val bio = "Tioadasdadadada dadadad"
        val interest = listOf("makan", "minum")

        /*            val nameBody = name.toRequestBody()
                    val emailBody = email.toRequestBody()
                    val bioBody = bio.toRequestBody()
                    val interestBody = interest.joinToString(", ").toRequestBody()*/
        viewModel.addUser(name, email, bio, interest)


        viewModel.getAddUser().observe(this){ userResponse ->

            when (userResponse) {
                is UserResponse.Success -> {
                    // Show ID in Toast
                    showToast("User ID: ${userResponse.data.id}")
                    Log.d("UserDataActivity", "User ID: ${userResponse.data.id}")
                }

                is UserResponse.Error -> {
                    // Show error message in Toast
                    showToast("Error: ${userResponse.error}")
                    Log.e("UserDataActivity", "Error: ${userResponse.error}")
                }
            }
        }
    }
    private fun actionButton() {
        binding.buttonSave.setOnClickListener {
            val user = if (Build.VERSION.SDK_INT >= 33) {
                intent.getParcelableExtra(EXTRA_USER_WITH_ADDRESS, AddUserRequest::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(EXTRA_USER_WITH_ADDRESS)
            }
            AlertDialog.Builder(this).apply {
                Log.d("Save Genre AlertDialog", "Login dengan test Berhasil.")
                setTitle("DATA USER")
                setMessage("${user?.name}, ${user?.email}, ${user?.bio}, ${user?.interest}")
                setPositiveButton("yes") { _, _ ->
                    startActivity(Intent(this@SelectGenreActivity, MainActivity::class.java))
                }
                create()
                show()
            }

        }
    }

    private fun showToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_USER_WITH_ADDRESS = "extra_user_with_address"
    }
}

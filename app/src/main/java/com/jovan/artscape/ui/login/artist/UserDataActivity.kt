package com.jovan.artscape.ui.login.artist

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jovan.artscape.ViewModelFactory
import com.jovan.artscape.databinding.ActivityUserDataBinding
import com.jovan.artscape.remote.request.AddUserRequest

class UserDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDataBinding
    private val viewModel by viewModels<UserViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAdd.setOnClickListener {
            val name = "Tio"
            val email = "Tio@gmail.com"
            val bio = "Tioadasdadadada dadadad "
            val interest = listOf("makan", "minum")

/*            val nameBody = name.toRequestBody()
            val emailBody = email.toRequestBody()
            val bioBody = bio.toRequestBody()
            val interestBody = interest.joinToString(", ").toRequestBody()*/

            val requestBody = AddUserRequest(
                nama = "jopaniniboss",
                email = "jopaniniboss@gmail.com",
                deskripsi = "Eiyooo",
                minat = listOf("menari", "Noho")
            )
            viewModel.addUser(name, email, bio, interest)
        }

        viewModel.getAddUser().observe(this){ response ->
            response.let {
            }

        }
    }
}
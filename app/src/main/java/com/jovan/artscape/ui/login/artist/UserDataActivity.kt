package com.jovan.artscape.ui.login.artist

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jovan.artscape.ViewModelFactory
import com.jovan.artscape.databinding.ActivityUserDataBinding
import com.jovan.artscape.ui.login.address.AddAddressActivity

class UserDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDataBinding
    private val viewModel by viewModels<UserViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDataBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setMyButtonEnable()
        enableButton()
        actionSetup()
    }

    private fun enableButton() {
        binding.apply {
            edName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable) {
                    setMyButtonEnable()
                }
            })
            edBio.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable) {
                    setMyButtonEnable()
                }
            })

            edPhoneNumber.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable) {
                    setMyButtonEnable()
                }
            })
        }
    }

    fun setMyButtonEnable() {
        binding.apply {
            val isNameValid = edName.text.toString().isNotEmpty()
            val isBioValid = edBio.text.toString().isNotEmpty()

            buttonAddUser.isEnabled = isNameValid && isBioValid
        }
    }

    private fun actionSetup() {
        binding.apply {
            buttonAddUser.setOnClickListener {

                val token = intent.getStringExtra(EXTRA_ID_TOKEN).toString()

                Log.d("token USER DATA", token)
                val intent = Intent(this@UserDataActivity, AddAddressActivity::class.java)
                intent.putExtra(AddAddressActivity.EXTRA_ID_TOKEN, token)
                intent.putExtra(AddAddressActivity.EXTRA_NAME, edName.text.toString())
                intent.putExtra(AddAddressActivity.EXTRA_BIO, edBio.text.toString())
                startActivity(intent)
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }


    companion object{
        const val EXTRA_ID_TOKEN = "extra_id_token"
    }
}
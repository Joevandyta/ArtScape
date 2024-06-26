package com.jovan.artscape.ui.login.artist

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.jovan.artscape.R
import com.jovan.artscape.databinding.ActivityUserDataBinding
import com.jovan.artscape.ui.login.address.AddAddressActivity
import kotlinx.coroutines.launch

class UserDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDataBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDataBinding.inflate(layoutInflater)
        topActionBar()
        setContentView(binding.root)
        auth = Firebase.auth

        setMyButtonEnable()
        enableButton()
        actionSetup()

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    signOut()
                }
            },
        )
    }

    private fun topActionBar() {
        supportActionBar?.show()
        val toolbar: Toolbar = binding.userDataToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    private fun signOut() {
        lifecycleScope.launch {
            val credentialManager = CredentialManager.create(this@UserDataActivity)
            auth.signOut()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        }
        finish()
    }

    private fun enableButton() {
        binding.apply {
            edName.addTextChangedListener(
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

            val isNameValid = edName.text.toString().isNotEmpty()
            val isPhoneNumberValid = edPhoneNumber.text.toString().isNotEmpty()
            val isBioValid = edBio.text.toString().isNotEmpty()

            buttonAddUser.isEnabled = isNameValid && isBioValid && isPhoneNumberValid
        }
    }

    private fun actionSetup() {
        binding.apply {
            buttonAddUser.setOnClickListener {
                val token = intent.getStringExtra(EXTRA_ID_TOKEN).toString()
                val phoneNumber = getString(R.string.template_62, edPhoneNumber.text.toString())
                Log.d("token USER DATA", "$token $phoneNumber")

                val intent = Intent(this@UserDataActivity, AddAddressActivity::class.java)
                intent.putExtra(AddAddressActivity.EXTRA_ID_TOKEN, token)
                intent.putExtra(AddAddressActivity.EXTRA_NAME, edName.text.toString())
                intent.putExtra(AddAddressActivity.EXTRA_BIO, edBio.text.toString())
                intent.putExtra(AddAddressActivity.EXTRA_PHONE_NUMBER, phoneNumber)
                startActivity(intent)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_ID_TOKEN = "extra_id_token"
    }
}

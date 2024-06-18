package com.jovan.artscape.ui.main.account

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.jovan.artscape.R
import com.jovan.artscape.ViewModelFactory
import com.jovan.artscape.databinding.FragmentAccountBinding
import com.jovan.artscape.remote.response.ApiResponse
import com.jovan.artscape.ui.AboutActivity
import com.jovan.artscape.ui.NotificationActivity
import com.jovan.artscape.ui.TransactionActivity
import com.jovan.artscape.ui.login.LoginActivity
import com.jovan.artscape.ui.mypainting.MyPaintingActivity
import com.jovan.artscape.ui.profile.EditProfileActivity
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AccountFragment : Fragment(R.layout.fragment_account) {
    private val viewModel by viewModels<AccountViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var _binding: FragmentAccountBinding? = null
    private lateinit var auth: FirebaseAuth
    private val binding get() = _binding!!
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val firebaseUser = auth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountBinding.bind(view)
        updateProfile()
        actionSetup()
    }

    private fun actionSetup() {
        binding.apply {
            editProfileButton.setOnClickListener {
                Log.d("AccountFragment", "Edit Profile Button Clicked")
                startActivity(Intent(requireContext(), EditProfileActivity::class.java))
            }
            settingsCard.apply {
                cvNotification.setOnClickListener {
                    startActivity(Intent(requireContext(), NotificationActivity::class.java))
                }
                cvTransactionHistory.setOnClickListener {
                    startActivity(Intent(requireContext(), TransactionActivity::class.java))
                }

                cvMypainting.setOnClickListener {
                    startActivity(Intent(requireContext(), MyPaintingActivity::class.java))
                }
                cvAddress.setOnClickListener {
                    showToast("Change Address")
                }
                cvSupport.setOnClickListener {
                    showToast("Support Us")
                }
                cvAboutApp.setOnClickListener {
                    startActivity(Intent(requireContext(), AboutActivity::class.java))
                }
                cvLogout.setOnClickListener {
                    showToast("Logout")
                    viewModel.getSesion().observe(viewLifecycleOwner) {
                        AlertDialog.Builder(requireContext()).apply {
                            Log.d("Logout AlertDialog", "TOKEN: ${it.token}")
                            setTitle("Logging Out")
                            setMessage("Are you?")
                            setPositiveButton("yes") { _, _ ->
                                signOut()
                            }
                            setNegativeButton("no") { _, _ ->
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

    private fun updateProfile() {
        binding.apply {
            viewModel.apply {
                getSesion().observe(viewLifecycleOwner) {
                    setUserData(it.uid)
                    getUserData().observe(viewLifecycleOwner) { userData ->
                        when (userData) {
                            is ApiResponse.Success -> {
                                tvUsernameUpdate.text = userData.data.name
                                tvPhoneNumber.text = userData.data.phoneNumber
                                tvBioUpdate.text = userData.data.description
                                Glide
                                    .with(requireContext())
                                    .load(userData.data.picture)
                                    .into(imgPicture)
                            }

                            is ApiResponse.Error -> {
                                if (userData.error.contains("User not found")) {
                                    signOut()
                                }
                                Log.d("AccountFragment", "Error: ${userData.error}")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun signOut() {
        lifecycleScope.launch {
            viewModel.logout()
            val credentialManager = CredentialManager.create(requireContext())
            auth.signOut()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}

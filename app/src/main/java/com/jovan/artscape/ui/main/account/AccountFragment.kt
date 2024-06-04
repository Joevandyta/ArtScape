package com.jovan.artscape.ui.main.account

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.jovan.artscape.R
import com.jovan.artscape.databinding.FragmentAccountBinding
import com.jovan.artscape.ui.AboutActivity
import com.jovan.artscape.ui.NotificationActivity
import com.jovan.artscape.ui.TransactionActivity
import com.jovan.artscape.ui.login.LoginActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AccountFragment : Fragment(R.layout.fragment_account) {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountBinding.bind(view)

        binding.settingsCard.apply {
            cvNotification.setOnClickListener {
                startActivity(Intent(requireContext(), NotificationActivity::class.java))
                showToast("Ini Notif")
                true
            }
            cvTransactionHistory.setOnClickListener {
                startActivity(Intent(requireContext(), TransactionActivity::class.java))
                showToast("Transaksi")
                true
            }
            cvAddress.setOnClickListener {
                showToast("Ini Alamat")
            }
            cvSupport.setOnClickListener {
                showToast("Ini Pembayaran")
            }
            cvAboutApp.setOnClickListener {
                startActivity(Intent(requireContext(), AboutActivity::class.java))
                showToast("Ini about")
                true
            }
            cvLogout.setOnClickListener {
                showToast("Logout")
                AlertDialog.Builder(requireContext()).apply {
                    Log.d("Logout AlertDialog", "Login dengan test Berhasil.")
                    setTitle("Logging Out")
                    setMessage("Are you?")
                    setPositiveButton("yes") { _, _ ->
                        startActivity(Intent(requireContext(), LoginActivity::class.java))

                        showLoading(false)

                    }
                    setNegativeButton("no") { _, _ ->
                        showLoading(false)
                    }
                    setCancelable(false)
                    create()
                    show()
                }

            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToast(text: String){
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
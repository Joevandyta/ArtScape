package com.jovan.artscape.ui.mypainting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.jovan.artscape.ViewModelFactory
import com.jovan.artscape.databinding.ActivityMyPaintingBinding
import com.jovan.artscape.remote.response.ApiResponse
import com.jovan.artscape.ui.adapter.PaintingListAdapter
import com.jovan.artscape.ui.mypainting.detail.MyPaintingDetailsActivity
import com.jovan.artscape.utils.DialogUtils
import com.jovan.artscape.utils.NetworkUtils

class MyPaintingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyPaintingBinding

    private val viewModel by viewModels<MyPaintingViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPaintingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (NetworkUtils.isNetworkAvailable(this))
            {
                topActionBar()
                viewModel.setAllPainting().apply {
                    showLoading(true)
                }
                adapterBind()
            } else {
            showLoading(false)
            Log.d("ERROR", "Network Not Available")
            DialogUtils.showNetworkSettingsDialog(this)
        }
    }

    private fun topActionBar() {
        supportActionBar?.show()
        val toolbar: Toolbar = binding.myPaintingToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun adapterBind() {
        val adapter = PaintingListAdapter()
        binding.rvMypainting.adapter = adapter
        binding.rvMypainting.layoutManager = LinearLayoutManager(this)
        viewModel.apply {
            getSesion().observe(this@MyPaintingActivity) { user ->
                getAllPainting().observe(this@MyPaintingActivity) {
                    when (it) {
                        is ApiResponse.Success -> {
                            Log.d("HomeFragment SUCCESS", "${it.data}")
                            showLoading(false)
                            binding.emptyPaintingContainer.visibility = View.GONE
                            adapter.setUserPaintingList(it.data, user.uid)

                            if (adapter.itemCount == 0) {
                                binding.emptyPaintingContainer.visibility = View.VISIBLE
                            }
                        }

                        is ApiResponse.Error -> {
                            Log.d("HomeFragment ERROR", it.error)
                            showLoading(false)
                        }
                    }
                }
            }
        }

        adapter.setOnItemClickCallBack(
            object : PaintingListAdapter.OnItemClickCallBack {
                override fun onItemClicked(paintingId: String) {
                    val intent = Intent(this@MyPaintingActivity, MyPaintingDetailsActivity::class.java)
                    intent.putExtra("PAINTING_ID", paintingId)
                    startActivity(intent)
                }
            },
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                showToast("Back")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}

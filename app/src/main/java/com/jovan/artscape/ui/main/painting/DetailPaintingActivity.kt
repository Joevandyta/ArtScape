package com.jovan.artscape.ui.main.painting

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.jovan.artscape.R
import com.jovan.artscape.ViewModelFactory
import com.jovan.artscape.databinding.ActivityDetailPaintingBinding
import com.jovan.artscape.remote.response.ApiResponse
import com.jovan.artscape.remote.response.painting.PaintingDetailsResponse

class DetailPaintingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPaintingBinding

    private val viewModel by viewModels<DetailPaintingViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPaintingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        topActionBar()

        val paintingId = intent.getStringExtra("PAINTING_ID") ?: return
        viewModel.setDetailPainting(paintingId)
        viewModel.getDetailResponse().observe(this) { response ->
            if (response.isSuccessful) {
                val paintingDetails = response.body()
                paintingDetails?.let {
                    bindData(it)
                }
            } else {
                showToast("Failed to fetch data")
            }
        }

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // back button pressed...
                    finish()
                }
            },
        )
    }

    private fun bindData(details: PaintingDetailsResponse) {
        binding.apply {
            tvPaintingTitle.text = details.title
            tvPaintingPrice.text = getString(R.string.rp, details.price.toString())

            viewModel.setUserData(details.artistId)
            viewModel.getUserData().observe(this@DetailPaintingActivity) {
                when (it) {
                    is ApiResponse.Success -> {
                        tvArtistName.text = it.data.name
                        tvArtistAddress.text = it.data.address
                    }

                    is ApiResponse.Error -> {
                        onBackPressedDispatcher.onBackPressed()
                        showToast("Error Adding Data")
                    }
                }
            }
            tvGenre.text = details.genre
            tvCreateAt.text = details.yearCreated.toString()
            tvDescription.text = details.description

            Glide
                .with(this@DetailPaintingActivity)
                .load(details.photo)
                .placeholder(R.drawable.painting_dummy) // Placeholder sementara gambar di-load
                .into(binding.imgPainting)
        }
    }

    private fun topActionBar() {
        supportActionBar?.show()
        supportActionBar?.title = "Upload Art"
        val toolbar: Toolbar = binding.detailPaintingToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
}

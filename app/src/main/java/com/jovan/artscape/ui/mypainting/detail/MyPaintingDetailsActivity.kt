package com.jovan.artscape.ui.mypainting.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.jovan.artscape.R
import com.jovan.artscape.ViewModelFactory
import com.jovan.artscape.databinding.ActivityMyPaintingDetailsBinding
import com.jovan.artscape.remote.response.ApiResponse
import com.jovan.artscape.remote.response.painting.PaintingDetailsResponse

class MyPaintingDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyPaintingDetailsBinding
    private val viewModel by viewModels<MyPaintingDetailsViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPaintingDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        topActionBar()

        val paintingId = intent.getStringExtra("PAINTING_ID")
        viewModel.setDetailPainting(paintingId.toString())
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
            tvArtist.text = details.artistId
            viewModel.setUserData(details.artistId)
            viewModel.getUserData().observe(this@MyPaintingDetailsActivity) {
                when (it) {
                    is ApiResponse.Success -> {
                        tvArtist.text = it.data.name
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
                .with(this@MyPaintingDetailsActivity)
                .load(details.photo)
                .placeholder(R.drawable.painting_dummy) // Placeholder sementara gambar di-load
                .into(ivPainting)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate menu resource
        menuInflater.inflate(R.menu.my_painting_menu, menu)
        return true
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
                true
            }

            R.id.edit_painting -> {
                showToast("edit painting activity")
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}

package com.jovan.artscape.ui.main.painting

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jovan.artscape.R
import com.jovan.artscape.ViewModelFactory
import com.jovan.artscape.databinding.ActivityDetailPaintingBinding
import com.jovan.artscape.remote.response.ApiResponse
import com.jovan.artscape.remote.response.painting.PaintingDetailsResponse
import com.jovan.artscape.ui.CartActivity

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
        bottomAppbar()

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
                        Glide
                            .with(this@DetailPaintingActivity)
                            .load(it.data.picture)
                            .placeholder(R.drawable.painting_dummy)
                            .into(imgArtist)
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
                .placeholder(R.drawable.painting_dummy)
                .into(binding.imgPainting)
        }
    }
    private fun  bottomAppbar() {
        binding.btnCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
        binding.btnAddComment.setOnClickListener{
            showAddCommentDialog()
        }
    }
    private fun showAddCommentDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val inflater = LayoutInflater.from(this)
        val view: View = inflater.inflate(R.layout.dialog_add_comment, null)
        dialog.setContentView(view)

        val window: Window? = dialog.window
        if (window != null) {
            val params: WindowManager.LayoutParams = window.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            window.attributes = params
        }

        val commentsList = view.findViewById<RecyclerView>(R.id.comments_list)
        val editTextComment = view.findViewById<EditText>(R.id.editTextComment)
        val btnSendComment = view.findViewById<ImageButton>(R.id.btn_send_comment)
        btnSendComment.setOnClickListener {
            val comment = editTextComment.text.toString()
        }
        dialog.show()
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

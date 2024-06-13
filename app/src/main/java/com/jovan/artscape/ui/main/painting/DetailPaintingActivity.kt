package com.jovan.artscape.ui.main.painting

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.jovan.artscape.databinding.ActivityDetailPaintingBinding

class DetailPaintingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPaintingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPaintingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        topActionBar()

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

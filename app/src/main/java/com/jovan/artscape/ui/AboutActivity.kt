package com.jovan.artscape.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.jovan.artscape.databinding.ActivityAboutAppBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutAppBinding.inflate(layoutInflater)
        topActionBar()
        setContentView(binding.root)
    }

    private fun topActionBar() {
        supportActionBar?.show()
        val toolbar: Toolbar = binding.toolbarAbout
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

package com.jovan.artscape.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.jovan.artscape.databinding.ActivityTransactionBinding

class TransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        topActionBar()
        setContentView(binding.root)
    }

    private fun topActionBar() {
        supportActionBar?.show()
        val toolbar: Toolbar = binding.transactionToolbar
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

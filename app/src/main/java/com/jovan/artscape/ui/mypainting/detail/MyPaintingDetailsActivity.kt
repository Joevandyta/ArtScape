package com.jovan.artscape.ui.mypainting.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jovan.artscape.databinding.ActivityMyPaintingDetailsBinding

class MyPaintingDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyPaintingDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}

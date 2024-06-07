package com.jovan.artscape.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.jovan.artscape.R
import com.jovan.artscape.databinding.ActivitySelectGenreBinding
import com.jovan.artscape.ui.main.MainActivity

class SelectGenreActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectGenreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_genre)

        val toolbar: Toolbar = findViewById(R.id.toolbar_genre)
        setSupportActionBar(toolbar)

        val chipGroup: ChipGroup = findViewById(R.id.chipGroup)
        val textView: TextView = findViewById(R.id.tv)

//        actionButton()

        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isEmpty()) {
                textView.text = "No genre selected"
            } else {
                val selectedGenres = checkedIds.joinToString(", ") { id ->
                    val chip: Chip = findViewById(id)
                    chip.text
                }
                textView.text = "Selected genres: $selectedGenres"
            }
        }
    }
//    private fun actionButton() {
//        binding.buttonSelect.setOnClickListener {
//            startActivity(Intent(this, MainActivity::class.java))
//        }
//    }
}

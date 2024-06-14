package com.jovan.artscape.ui.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.jovan.artscape.ViewModelFactory
import com.jovan.artscape.databinding.ActivitySearchBinding
import com.jovan.artscape.remote.response.ApiResponse
import com.jovan.artscape.remote.response.user.AllUserResponse
import com.jovan.artscape.ui.adapter.PaintingListAdapter
import com.jovan.artscape.ui.adapter.UserListAdapter

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val viewModel by viewModels<SearchViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionButton()
        val searchView = binding.searchView
        searchView.clearFocus()
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.setSearch(newText ?: "")
                    searchResponse()
                    return true
                }
            },
        )
    }

    private fun searchResponse() {
        val paintingAdapter = PaintingListAdapter()
        val userAdapter = UserListAdapter()
        binding.apply {
            rvPainting.adapter = paintingAdapter
            rvPainting.layoutManager = LinearLayoutManager(this@SearchActivity)

            rvArtist.adapter = userAdapter
            rvArtist.layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        viewModel.getSearch().observe(this) {
            when (it) {
                is ApiResponse.Success -> {
                    Log.d("HomeFragment SUCCESS", "${it.data}")
                    showLoading(false)
                    paintingAdapter.setHomePaintingList(it.data.listPainting)
                    userAdapter.setUserList(it.data.listUser)
                    Log.d("UserAdapter", "${userAdapter.itemCount}")
                    Log.d("paintingAdapter", "${paintingAdapter.itemCount}")

                    if (paintingAdapter.itemCount == 0) {
                        binding.tvPaintingEmpty.visibility = View.VISIBLE
                    } else {
                        binding.tvPaintingEmpty.visibility = View.GONE
                    }

                    if (userAdapter.itemCount == 0) {
                        binding.tvArtistEmpty.visibility = View.VISIBLE
                    } else {
                        binding.tvArtistEmpty.visibility = View.GONE
                    }

                    paintingAdapter.setOnItemClickCallBack(
                        object : PaintingListAdapter.OnItemClickCallBack {
                            override fun onItemClicked(paintingId: String) {
                                showToast("$paintingId Clicked")
                            }
                        },
                    )

                    userAdapter.setOnItemClickCallBack(
                        object : UserListAdapter.OnItemClickCallBack {
                            override fun onItemClicked(data: AllUserResponse) {
                                showToast("${data.name} Clicked")
                            }
                        },
                    )
                }

                is ApiResponse.Error -> {
                    Log.d("HomeFragment ERROR", it.error)
                    showLoading(false)
                    paintingAdapter.setHomePaintingList(emptyList())
                    userAdapter.setUserList(emptyList())
                    // Update the visibility of the empty views
                    binding.tvPaintingEmpty.visibility = View.VISIBLE
                    binding.tvArtistEmpty.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun actionButton() {
        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}

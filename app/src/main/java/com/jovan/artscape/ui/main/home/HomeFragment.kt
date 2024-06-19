package com.jovan.artscape.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jovan.artscape.R
import com.jovan.artscape.ViewModelFactory
import com.jovan.artscape.databinding.FragmentHomeBinding
import com.jovan.artscape.remote.request.RecommendationsPaintingRequest
import com.jovan.artscape.remote.response.ApiResponse
import com.jovan.artscape.ui.CartActivity
import com.jovan.artscape.ui.NotificationActivity
import com.jovan.artscape.ui.adapter.PaintingListAdapter
import com.jovan.artscape.ui.main.painting.DetailPaintingActivity
import com.jovan.artscape.ui.search.SearchActivity

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private val binding get() = _binding!!
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        toolbar = binding.homeToolbar
        viewModel.setAllPainting().apply {
            showLoading(true)
        }
        recommendPainting()
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        adapterBind()
        topAppBar()
    }

    private fun recommendPainting() {
        viewModel.apply {
            getSession().observe(viewLifecycleOwner) {
                setUserData(it.uid)
            }

            getUserData().observe(viewLifecycleOwner) { getUserData ->
                when (getUserData) {
                    is ApiResponse.Success -> {
                        val interests = getUserData.data.interest
                        val ratings =
                            interests!!
                                .mapIndexed { index, genre ->
                                    listOf(genre, 5 - index)
                                }.take(5)
                        val request =
                            RecommendationsPaintingRequest(
                                ratings = ratings,
                                numRecommendations = 3,
                            )

                        setPaintingRecommendation(request)
                        Log.d("HomeFragment", "onViewCreated: $request")
                    }

                    is ApiResponse.Error -> {
                        Log.d("getUserData", getUserData.error)
                    }
                }
            }
        }
    }

    private fun adapterBind() {
        val adapter = PaintingListAdapter()
        adapter.setOnItemClickCallBack(
            object : PaintingListAdapter.OnItemClickCallBack {
                override fun onItemClicked(paintingId: String) {
                    val intent = Intent(requireContext(), DetailPaintingActivity::class.java)
                    intent.putExtra("PAINTING_ID", paintingId)
                    startActivity(intent)
                }
            },
        )
        binding.rvArt.adapter = adapter
        binding.rvArt.layoutManager = LinearLayoutManager(requireContext())
        viewModel.apply {
            getAllPainting().observe(viewLifecycleOwner) { allPainting ->
                when (allPainting) {
                    is ApiResponse.Success -> {
                        Log.d("HomeFragment SUCCESS", "${allPainting.data}")
                        showLoading(false)
                        getPaintingRecommendation().observe(viewLifecycleOwner) { paintingRecomend ->
                            when (paintingRecomend) {
                                is ApiResponse.Success -> {
                                    adapter.setRecomendedPaintingList(allPainting.data, paintingRecomend.data.recommendations)
                                    Log.d("getPaintingRecomendation SUCCESS", "${paintingRecomend.data}")
                                    if (adapter.itemCount == 0) {
                                        Toast
                                            .makeText(
                                                requireContext(),
                                                "Data doesnt Exist",
                                                Toast.LENGTH_SHORT,
                                            ).show()
                                    }
                                }
                                is ApiResponse.Error -> {
                                    Log.d("HomeFragment ERROR", paintingRecomend.error)
                                    adapterBind()
                                }
                            }
                        }
                    }

                    is ApiResponse.Error -> {
                        Log.d("HomeFragment ERROR", allPainting.error)
                        showLoading(false)
                        adapterBind()
                    }
                }
            }
        }
    }

    private fun topAppBar() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(
                    menu: Menu,
                    menuInflater: MenuInflater,
                ) {
                    menuInflater.inflate(R.menu.top_action_bar, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    // Handle the menu selection
                    return when (menuItem.itemId) {
                        R.id.bar_cart -> {
                            startActivity(Intent(requireContext(), CartActivity::class.java))
                            true
                        }

                        R.id.bar_notification -> {
                            startActivity(
                                Intent(
                                    requireContext(),
                                    NotificationActivity::class.java,
                                ),
                            )
                            true
                        }

                        R.id.bar_search -> {
                            startActivity(Intent(requireContext(), SearchActivity::class.java))
                            Log.d("HomeFragment", "onOptionsItemSelected: Search")
                            true
                        }

                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED,
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}

package com.jovan.artscape.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.jovan.artscape.data.ProvideRepository
import com.jovan.artscape.remote.response.ApiResponse
import com.jovan.artscape.remote.response.ErrorResponse
import com.jovan.artscape.remote.response.SearchResponse
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: ProvideRepository,
) : ViewModel() {
    private val searchResponse = MutableLiveData<ApiResponse<SearchResponse>>()

    fun setSearch(query: String) {
        viewModelScope.launch {
            val response = repository.search(query)
            if (response.isSuccessful) {
                searchResponse.value = ApiResponse.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                searchResponse.value =
                    ApiResponse.Error(errorResponse.error, errorResponse.details ?: "")
                Log.d("RESPONSE notSuccessful", "addUser: ${errorResponse.error}")
            }
        }
    }

    fun getSearch(): LiveData<ApiResponse<SearchResponse>> = searchResponse
}

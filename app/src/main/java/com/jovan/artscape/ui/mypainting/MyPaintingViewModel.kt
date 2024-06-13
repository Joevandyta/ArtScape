package com.jovan.artscape.ui.mypainting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.jovan.artscape.data.ProvideRepository
import com.jovan.artscape.data.pref.UserModel
import com.jovan.artscape.remote.response.ApiResponse
import com.jovan.artscape.remote.response.ErrorResponse
import com.jovan.artscape.remote.response.painting.AllPaintingResponse
import kotlinx.coroutines.launch

class MyPaintingViewModel(
    private val repository: ProvideRepository,
) : ViewModel() {
    private val paintingResponse = MutableLiveData<ApiResponse<List<AllPaintingResponse>>>()

    fun getSesion(): LiveData<UserModel> = repository.getSession().asLiveData()

    fun setAllPainting() {
        viewModelScope.launch {
            Log.d("PARAM", "SetAllPainting")
            val response = repository.getAllpainting()
            if (response.isSuccessful) {
                paintingResponse.value = ApiResponse.Success(response.body()!!)
                Log.d("RESPONSE isSuccessful", "addUser: ${response.body()}")
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                paintingResponse.value = ApiResponse.Error(errorResponse.error, errorResponse.details ?: "")
                Log.d("RESPONSE notSuccessful", "addUser: ${errorResponse.error}")
            }
        }
    }

    fun getAllPainting(): MutableLiveData<ApiResponse<List<AllPaintingResponse>>> = paintingResponse
}

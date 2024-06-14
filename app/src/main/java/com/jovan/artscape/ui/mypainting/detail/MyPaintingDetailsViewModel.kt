package com.jovan.artscape.ui.mypainting.detail

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
import com.jovan.artscape.remote.response.painting.PaintingDetailsResponse
import com.jovan.artscape.remote.response.user.AllUserResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class MyPaintingDetailsViewModel(
    private val repository: ProvideRepository,
) : ViewModel() {
    private val paintingResponse = MutableLiveData<Response<PaintingDetailsResponse>>()
    private val userDataResponse = MutableLiveData<ApiResponse<AllUserResponse>>()

    fun getSession(): LiveData<UserModel> = repository.getSession().asLiveData()

    fun setDetailPainting(id: String) {
        viewModelScope.launch {
            try {
                val response = repository.getPaintingDetail(id)
                paintingResponse.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getDetailResponse(): LiveData<Response<PaintingDetailsResponse>> = paintingResponse

    fun setUserData(id: String) {
        viewModelScope.launch {
            val response = repository.getUserData(id)
            if (response.isSuccessful) {
                userDataResponse.value = ApiResponse.Success(response.body()!!)
                Log.d("RESPONSE isSuccessful", "User: ${response.body()}")
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                userDataResponse.value = ApiResponse.Error(errorResponse.error, errorResponse.details ?: "")
                Log.d("RESPONSE notSuccessful", "ERROR: ${errorResponse.error}")
            }
        }
    }

    fun getUserData(): MutableLiveData<ApiResponse<AllUserResponse>> = userDataResponse
}

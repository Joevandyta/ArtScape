package com.jovan.artscape.ui.main.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.jovan.artscape.data.ProvideRepository
import com.jovan.artscape.data.pref.UserModel
import com.jovan.artscape.remote.request.RecommendationsPaintingRequest
import com.jovan.artscape.remote.response.ApiResponse
import com.jovan.artscape.remote.response.ErrorResponse
import com.jovan.artscape.remote.response.RecommendationsPaintingResponse
import com.jovan.artscape.remote.response.painting.AllPaintingResponse
import com.jovan.artscape.remote.response.user.AllUserResponse
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: ProvideRepository,
) : ViewModel() {
    private val paintingResponse = MutableLiveData<ApiResponse<List<AllPaintingResponse>>>()
    private val paintingRecomendationResponse = MutableLiveData<ApiResponse<RecommendationsPaintingResponse>>()
    private val userDataResponse = MutableLiveData<ApiResponse<AllUserResponse>>()

    fun getSession(): LiveData<UserModel> = repository.getSession().asLiveData()

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

    fun setPaintingRecommendation(paintingRequest: RecommendationsPaintingRequest) {
        viewModelScope.launch {
            Log.d("PARAM", "SetAllPainting")
            try {
                val response = repository.recommendPainting(paintingRequest)
                if (response.isSuccessful) {
                    paintingRecomendationResponse.value = ApiResponse.Success(response.body()!!)
                    Log.d("RESPONSE isSuccessful", "SET PAINTING RECOMMEND: ${response.body()}")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                    paintingRecomendationResponse.value = ApiResponse.Error(errorResponse.error, errorResponse.details ?: "")
                    Log.d("RESPONSE notSuccessful", "SET PAINTING RECOMMEND: ${errorResponse.error}")
                }
            } catch (e: Exception) {
                Log.d("ERROR", "setPaintingRecomendation: ${e.message}")
                paintingRecomendationResponse.value = ApiResponse.Error(e.message.toString(), "")
            }
        }
    }

    fun getPaintingRecommendation(): MutableLiveData<ApiResponse<RecommendationsPaintingResponse>> = paintingRecomendationResponse

    fun setUserData(id: String) {
        viewModelScope.launch {
            try {
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
            } catch (e: Exception) {
                userDataResponse.value = ApiResponse.Error(e.message.toString(), "")
            }
        }
    }

    fun getUserData(): MutableLiveData<ApiResponse<AllUserResponse>> = userDataResponse
}

package com.jovan.artscape.ui.upload

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
import com.jovan.artscape.remote.response.ClasificationListResponse
import com.jovan.artscape.remote.response.ErrorResponse
import com.jovan.artscape.remote.response.painting.UploadResponseSuccess
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadViewModel(
    private val repository: ProvideRepository,
) : ViewModel() {
    private val uploadResponse = MutableLiveData<ApiResponse<UploadResponseSuccess>>()
    private val genreResponse = MutableLiveData<ApiResponse<ClasificationListResponse>>()

    fun getSession(): LiveData<UserModel> = repository.getSession().asLiveData()

    fun setUploadPainting(
        photo: MultipartBody.Part,
        title: RequestBody,
        description: RequestBody,
        media: RequestBody,
        genre: RequestBody,
        price: RequestBody,
        yearCreated: RequestBody,
        artistId: RequestBody,
    ) {
        viewModelScope.launch {
            val response = repository.uploadPainting(photo, title, description, media, genre, price, yearCreated, artistId)

            Log.d("PARAM", "addUser: $yearCreated")
            if (response.isSuccessful) {
                uploadResponse.value = ApiResponse.Success(response.body()!!)
                Log.d("RESPONSE isSuccessful", "addUser: ${response.body()}")
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                uploadResponse.value =
                    ApiResponse.Error(errorResponse.error, errorResponse.details ?: "")
                Log.d("RESPONSE notSuccessful", "addUser: ${errorResponse.error}")
            }
        }
    }

    fun getUploadResponse(): LiveData<ApiResponse<UploadResponseSuccess>> = uploadResponse

    fun setGenreClasification(painting: MultipartBody.Part) {
        viewModelScope.launch {
            try {
                val response = repository.genreClasification(painting)
                Log.d("PARAM", "setGenreClasification")
                if (response.isSuccessful) {
                    genreResponse.value = ApiResponse.Success(response.body()!!)
                    Log.d("RESPONSE isSuccessful", "setGenreClasification: ${response.body()}")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                    genreResponse.value =
                        ApiResponse.Error(errorResponse.error, errorResponse.details ?: "")
                    Log.d("RESPONSE notSuccessful", "setGenreClasification: ${errorResponse.error}")
                }
            } catch (e: Exception) {
                genreResponse.value = ApiResponse.Error(e.message.toString(), "")
            }
        }
    }

    fun getGenreClasification(): LiveData<ApiResponse<ClasificationListResponse>> = genreResponse
}

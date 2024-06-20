package com.jovan.artscape.ui.main

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
import com.jovan.artscape.remote.response.user.AllUserResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MainViewModel(
    private val repository: ProvideRepository,
) : ViewModel() {
    fun getSession(): LiveData<UserModel> = repository.getSession().asLiveData()

    private val userDataResponse = MutableLiveData<ApiResponse<AllUserResponse>>()

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun setUserData(id: String) {
        viewModelScope.launch {
            val response = repository.getUserData(id)
            try {
                if (response.isSuccessful) {
                    userDataResponse.value = ApiResponse.Success(response.body()!!)
                    Log.d("RESPONSE isSuccessful", "User: ${response.body()}")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                    userDataResponse.value =
                        ApiResponse.Error(errorResponse.error, errorResponse.details ?: "")
                    Log.d("RESPONSE notSuccessful", "ERROR: ${errorResponse.error}")
                }
            } catch (e: UnknownHostException) {
                e.printStackTrace()
                userDataResponse.value = ApiResponse.Error("No internet connection", "")
                Log.d("NETWORK_ERROR", "No internet connection")
            } catch (e: SocketTimeoutException) {
                e.printStackTrace()
                userDataResponse.value = ApiResponse.Error("Request timed out", "")
                Log.d("TIMEOUT_ERROR", "Request timed out")
            } catch (e: HttpException) {
                e.printStackTrace()
                userDataResponse.value = ApiResponse.Error("Server error", e.message ?: "")
                Log.d("HTTP_ERROR", "Server error: ${e.message}")
            } catch (e: Exception) {
                e.printStackTrace()
                userDataResponse.value =
                    ApiResponse.Error("An unexpected error occurred", e.message ?: "")
                Log.d("UNKNOWN_ERROR", "An unexpected error occurred: ${e.message}")
            }
        }
    }

    fun getUserData(): MutableLiveData<ApiResponse<AllUserResponse>> = userDataResponse
}

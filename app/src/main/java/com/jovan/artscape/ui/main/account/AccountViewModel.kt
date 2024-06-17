package com.jovan.artscape.ui.main.account

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

class AccountViewModel(
    private val repository: ProvideRepository,
) : ViewModel() {
    val userDataResponse = MutableLiveData<ApiResponse<AllUserResponse>>()

    fun getSesion(): LiveData<UserModel> = repository.getSession().asLiveData()

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

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

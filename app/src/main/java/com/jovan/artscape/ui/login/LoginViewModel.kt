package com.jovan.artscape.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.jovan.artscape.data.ProvideRepository
import com.jovan.artscape.data.pref.UserModel
import com.jovan.artscape.remote.request.LoginRequest
import com.jovan.artscape.remote.response.ApiResponse
import com.jovan.artscape.remote.response.ErrorResponse
import com.jovan.artscape.remote.response.user.UserResponseSuccess
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: ProvideRepository,
) : ViewModel() {
    private val apiResponse = MutableLiveData<ApiResponse<UserResponseSuccess>>()

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun getSession(): LiveData<UserModel> = repository.getSession().asLiveData()

    fun setLogin(idToken: String) {
        viewModelScope.launch {
            try {
                Log.d("PARAM", "addUser: $idToken")
                val response = repository.setlogin(LoginRequest(idToken))
                Log.d("RESPONSE setLogin", "addUser: ${response.body()}")

                if (response.isSuccessful) {
                    apiResponse.value = ApiResponse.Success(response.body()!!)
                    Log.d("RESPONSE isSuccessful", "addUser: ${response.body()}")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                    apiResponse.value =
                        ApiResponse.Error(errorResponse.error, errorResponse.details ?: "")
                    Log.d("RESPONSE notSuccessful", "addUser: ${errorResponse.error}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                apiResponse.value =
                    ApiResponse.Error("An unexpected error occurred", e.message ?: "")
                Log.d("UNKNOWN_ERROR", "An unexpected error occurred: ${e.message}")
            }
        }
    }

    fun getLogin(): MutableLiveData<ApiResponse<UserResponseSuccess>> = apiResponse
}

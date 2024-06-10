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
import com.jovan.artscape.remote.response.ErrorResponse
import com.jovan.artscape.remote.response.SuccessResponse
import com.jovan.artscape.remote.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val repository: ProvideRepository) : ViewModel() {
    private val userResponse = MutableLiveData<UserResponse<SuccessResponse>>()
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    suspend fun getSessionSuspend(): LiveData<UserModel> {
        return withContext(Dispatchers.IO) {
            // Simulate fetching session data
            // Replace with actual fetching logic
            delay(1000)
            repository.getSession().asLiveData() // Replace with actual session data
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun setLogin(idToken: String) {
        viewModelScope.launch {
            Log.d("PARAM", "addUser: $idToken")
            val response = repository.setlogin(LoginRequest(idToken))
            Log.d("RESPONSE setLogin", "addUser: ${response.body()}")

            if (response.isSuccessful) {
                userResponse.value = UserResponse.Success(response.body()!!)
                Log.d("RESPONSE isSuccessful", "addUser: ${response.body()}")

            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                userResponse.value =
                    UserResponse.Error(errorResponse.error, errorResponse.details ?: "")
                Log.d("RESPONSE notSuccessful", "addUser: ${errorResponse.error}")
            }
        }
    }

    fun getLogin(): MutableLiveData<UserResponse<SuccessResponse>> {
        return userResponse
    }
}
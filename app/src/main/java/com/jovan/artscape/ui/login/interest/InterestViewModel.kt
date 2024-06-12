package com.jovan.artscape.ui.login.interest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.jovan.artscape.data.ProvideRepository
import com.jovan.artscape.data.pref.UserModel
import com.jovan.artscape.remote.request.AddUserRequest
import com.jovan.artscape.remote.response.ApiResponse
import com.jovan.artscape.remote.response.ErrorResponse
import com.jovan.artscape.remote.response.user.UserResponseSuccess
import kotlinx.coroutines.launch
import retrofit2.HttpException

class InterestViewModel(private val repository: ProvideRepository): ViewModel() {
    private val apiResponse = MutableLiveData<ApiResponse<UserResponseSuccess>>()

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
    fun addUser(addUserRequest: AddUserRequest){
        viewModelScope.launch {
            try {
                val response = repository.addUserData(addUserRequest = addUserRequest)
                if (response.isSuccessful) {
                    apiResponse.value = ApiResponse.Success(response.body()!!)

                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                    ApiResponse.Error(errorResponse.error, errorResponse.details?:"")
                    apiResponse.value = ApiResponse.Error(errorResponse.error, errorResponse.details?:"")
                }
            } catch (e: HttpException) {
                apiResponse.value = ApiResponse.Error("Error True", e.message())
            } catch (e: Exception) {
                apiResponse.value = ApiResponse.Error("Error Exeption", e.message ?: "An unknown error occurred")
            }
        }
    }
    fun getAddUser():MutableLiveData<ApiResponse<UserResponseSuccess>>{
        return apiResponse
    }
}
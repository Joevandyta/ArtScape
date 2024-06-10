package com.jovan.artscape.ui.login.artist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.jovan.artscape.data.ProvideRepository
import com.jovan.artscape.remote.api.RetrofiClient
import com.jovan.artscape.remote.request.AddUserRequest
import com.jovan.artscape.remote.response.ErrorResponse
import com.jovan.artscape.remote.response.SuccessResponse
import com.jovan.artscape.remote.response.UserResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class UserViewModel(private val repository: ProvideRepository): ViewModel() {
    private val userResponse = MutableLiveData<UserResponse<SuccessResponse>>()
    fun addUser(idToken: String, name: String, email: String, bio:String, interest: List<String>){
        viewModelScope.launch {

            Log.d("PARAM", "addUser: $name $email $bio $interest")
            try {
                val response = RetrofiClient.getApiArtSpace().addUserData(addUserRequest = AddUserRequest(idToken, name, email, bio, interest))
                if (response.isSuccessful) {
                    userResponse.value = UserResponse.Success(response.body()!!)

                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                    UserResponse.Error(errorResponse.error, errorResponse.details?:"")
                    userResponse.value = UserResponse.Error(errorResponse.error, errorResponse.details?:"")
                }
            } catch (e: HttpException) {

                userResponse.value = UserResponse.Error("Error True", e.message())
            } catch (e: Exception) {
                userResponse.value = UserResponse.Error("Error Exeption", e.message ?: "An unknown error occurred")
            }
        }
    }
    fun getAddUser():MutableLiveData<UserResponse<SuccessResponse>>{
        return userResponse
    }
}
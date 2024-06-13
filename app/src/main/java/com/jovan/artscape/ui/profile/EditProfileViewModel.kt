package com.jovan.artscape.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.jovan.artscape.data.ProvideRepository
import com.jovan.artscape.data.pref.UserModel
import com.jovan.artscape.remote.request.UpdateUserRequest
import com.jovan.artscape.remote.response.ApiResponse
import com.jovan.artscape.remote.response.ErrorResponse
import com.jovan.artscape.remote.response.user.UserResponseSuccess
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val repository: ProvideRepository,
) : ViewModel() {
    private val userResponse = MutableLiveData<ApiResponse<UserResponseSuccess>>()

    fun getSession(): LiveData<UserModel> = repository.getSession().asLiveData()

    fun editUser(
        id: String,
        updateUserRequest: UpdateUserRequest,
    ) {
        viewModelScope.launch {
            val response = repository.editUser(id, updateUserRequest)
            if (response.isSuccessful) {
                userResponse.value = ApiResponse.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                userResponse.value =
                    ApiResponse.Error(errorResponse.error, errorResponse.details ?: "")
                Log.d("RESPONSE notSuccessful", "addUser: ${errorResponse.error}")
            }
        }
    }

    fun getUserResponse(): LiveData<ApiResponse<UserResponseSuccess>> = userResponse
}

package com.jovan.artscape.ui.login.artist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jovan.artscape.data.ProvideRepository
import com.jovan.artscape.remote.response.ApiResponse
import com.jovan.artscape.remote.response.user.UserResponseSuccess

class UserViewModel(private val repository: ProvideRepository): ViewModel() {
    private val apiResponse = MutableLiveData<ApiResponse<UserResponseSuccess>>()

    fun getAddUser():MutableLiveData<ApiResponse<UserResponseSuccess>>{
        return apiResponse
    }
}
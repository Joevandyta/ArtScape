package com.jovan.artscape.ui.login.artist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jovan.artscape.data.ProvideRepository
import com.jovan.artscape.remote.response.SuccessResponse
import com.jovan.artscape.remote.response.UserResponse

class UserViewModel(private val repository: ProvideRepository): ViewModel() {
    private val userResponse = MutableLiveData<UserResponse<SuccessResponse>>()

    fun getAddUser():MutableLiveData<UserResponse<SuccessResponse>>{
        return userResponse
    }
}
package com.jovan.artscape.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jovan.artscape.data.ProvideRepository
import com.jovan.artscape.data.pref.UserModel
import com.jovan.artscape.remote.request.AddUserRequest
import com.jovan.artscape.remote.response.ApiResponse
import com.jovan.artscape.remote.response.painting.UploadResponseSuccess
import com.jovan.artscape.remote.response.user.UserResponseSuccess
import kotlinx.coroutines.launch
import retrofit2.Response

class EditProfileViewModel (
    private val repository: ProvideRepository
) : ViewModel() {

    private val _userResponse = MutableLiveData<Response<UserResponseSuccess>>()
//    val userResponse: LiveData<Response<UserResponseSuccess>> get() = _userResponse

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun editUser(
        id:String,
        addUserRequest: AddUserRequest) {
        viewModelScope.launch {
            val response = repository.editUser(id, addUserRequest)
            _userResponse.value = response
        }
    }
    fun getUserResponse(): LiveData<Response<UserResponseSuccess>> {
        return _userResponse
    }
}
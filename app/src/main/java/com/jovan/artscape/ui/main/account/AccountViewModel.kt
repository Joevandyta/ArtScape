package com.jovan.artscape.ui.main.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jovan.artscape.data.ProvideRepository
import com.jovan.artscape.data.pref.UserModel
import com.jovan.artscape.remote.response.user.AllUserResponse
import kotlinx.coroutines.launch

class AccountViewModel(
    private val repository: ProvideRepository,
) : ViewModel() {
    fun getSesion(): LiveData<UserModel> = repository.getSession().asLiveData()

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getUserData(id: String): MutableLiveData<AllUserResponse> {
        val userDataResponse = MutableLiveData<AllUserResponse>()
        viewModelScope.launch {
            userDataResponse.value = repository.getUserData(id)
        }
        return userDataResponse
    }
}

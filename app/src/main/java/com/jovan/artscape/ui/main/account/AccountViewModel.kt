package com.jovan.artscape.ui.main.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jovan.artscape.data.ProvideRepository
import com.jovan.artscape.data.pref.UserModel
import kotlinx.coroutines.launch

class AccountViewModel(private val repository: ProvideRepository): ViewModel() {
    fun getSesion(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
package com.jovan.artscape.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jovan.artscape.data.ProvideRepository
import com.jovan.artscape.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: ProvideRepository): ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}
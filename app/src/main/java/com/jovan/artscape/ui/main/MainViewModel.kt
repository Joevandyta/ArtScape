package com.jovan.artscape.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jovan.artscape.data.ProvideRepository
import com.jovan.artscape.data.pref.UserModel

class MainViewModel(private val repository: ProvideRepository): ViewModel() {
    fun getSesion(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}
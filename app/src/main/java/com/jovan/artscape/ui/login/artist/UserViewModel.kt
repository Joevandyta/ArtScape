package com.jovan.artscape.ui.login.artist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jovan.artscape.data.ProvideRepository
import com.jovan.artscape.remote.request.AddUserRequest
import com.jovan.artscape.remote.response.user.SuccessResponse
import kotlinx.coroutines.launch

class UserViewModel(private val repository: ProvideRepository): ViewModel() {
    private val userResponse = MutableLiveData<SuccessResponse>()

    fun addUser(name: String, email: String, bio:String, interest: List<String>){
        viewModelScope.launch {
            Log.d("PARAM", "addUser: $name $email $bio $interest")
            try {
                val user = AddUserRequest(
                    nama = "jopaniniboss",
                    email = "joe@gmail.com",
                    deskripsi = "jopaniniboss@gmail.com",
                    minat = listOf("nyanyi")
                )
                val response = repository.addUser(user)
                Log.d("SUCCESS USERVIEW MODEL", "addUser: ${response}")
                userResponse.postValue(response)


            }catch (e:Exception){
                Log.d("ERROR USERVIEW MODEL", "addUser: ${e.message}")
            }
        }
    }
    fun getAddUser():MutableLiveData<SuccessResponse>{
        return userResponse
    }
}
package com.jovan.artscape.ui.main.painting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jovan.artscape.data.ProvideRepository
import com.jovan.artscape.data.pref.UserModel

import com.jovan.artscape.remote.response.painting.PaintingDetailsResponse
import kotlinx.coroutines.launch
import retrofit2.Response


class DetailPaintingViewModel(private val repository: ProvideRepository): ViewModel() {
    private val _response = MutableLiveData<Response<PaintingDetailsResponse>>()
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
    fun setDetailPainting(id:String) {
        viewModelScope.launch {
            try {
                val response = repository.getPaintingDetail(id)
                _response.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getDetailResponse(): LiveData<Response<PaintingDetailsResponse>> {
        return _response
    }
}
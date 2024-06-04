package com.jovan.artscape.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jovan.artscape.data.ProvideRepository
import com.jovan.artscape.remote.response.DistrictResponse
import com.jovan.artscape.remote.response.ProvinceResponse
import com.jovan.artscape.remote.response.RegenciesResponse
import com.jovan.artscape.remote.response.VillageResponse
import kotlinx.coroutines.launch

class AddressViewModel(private val repository: ProvideRepository):ViewModel() {
    private val provinceResponse = MutableLiveData<List<ProvinceResponse>>()
    private val regenciesResponse = MutableLiveData<List<RegenciesResponse>>()
    private val districtResponse = MutableLiveData<List<DistrictResponse>>()
    private val villageResponse = MutableLiveData<List<VillageResponse>>()

    fun getProvinces():MutableLiveData<List<ProvinceResponse>>{
        viewModelScope.launch {
            provinceResponse.value = repository.getProvinces()
        }
        return provinceResponse
    }

    fun setRegencies(id:String){
        viewModelScope.launch {
            regenciesResponse.value = repository.getRegencies(id)
        }
    }
    fun getRegencies():MutableLiveData<List<RegenciesResponse>>{
        return regenciesResponse
    }

    fun setDistricts(id:String){
        viewModelScope.launch {
            districtResponse.value = repository.getDistricts(id)
        }
    }
    fun getDistricts():MutableLiveData<List<DistrictResponse>>{
        return districtResponse
    }

    fun setVillages(id:String){
        viewModelScope.launch {
            villageResponse.value = repository.getVillages(id)
        }
    }
    fun getVillages():MutableLiveData<List<VillageResponse>>{
        return villageResponse
    }
}
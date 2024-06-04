package com.jovan.artscape.data

import com.jovan.artscape.remote.api.RetrofiClient
import com.jovan.artscape.remote.response.DistrictResponse
import com.jovan.artscape.remote.response.ProvinceResponse
import com.jovan.artscape.remote.response.RegenciesResponse
import com.jovan.artscape.remote.response.VillageResponse

class ProvideRepository private constructor(){
    suspend fun getProvinces(): List<ProvinceResponse> {
        return RetrofiClient.getApiService().getProvince()
    }

    suspend fun getRegencies(id: String): List<RegenciesResponse> {
        return RetrofiClient.getApiService().getRegencies(id)
    }

    suspend fun getDistricts(id: String): List<DistrictResponse> {
        return RetrofiClient.getApiService().getDistrict(id)
    }

    suspend fun getVillages(id: String): List<VillageResponse> {
        return RetrofiClient.getApiService().getVillage(id)
    }
    companion object {
        @Volatile
        private var instance: ProvideRepository? = null
        fun getInstance(

        ): ProvideRepository =
            instance ?: synchronized(this) {
                instance ?: ProvideRepository()
            }.also { instance = it }
    }
}
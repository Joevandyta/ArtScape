package com.jovan.artscape.data

import com.jovan.artscape.data.pref.ProvidePreference
import com.jovan.artscape.data.pref.UserModel
import com.jovan.artscape.remote.api.RetrofiClient
import com.jovan.artscape.remote.request.AddUserRequest
import com.jovan.artscape.remote.response.address.DistrictResponse
import com.jovan.artscape.remote.response.address.ProvinceResponse
import com.jovan.artscape.remote.response.address.RegenciesResponse
import com.jovan.artscape.remote.response.address.VillageResponse
import com.jovan.artscape.remote.response.user.SuccessResponse
import kotlinx.coroutines.flow.Flow

class ProvideRepository private constructor(
    private var providePreference: ProvidePreference
){

    //Session DataStore
    suspend fun saveSession(user: UserModel) {
        providePreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return providePreference.getSession()
    }

    suspend fun logout() {
        providePreference.logout()
    }

    //API Region
    suspend fun getProvinces(): List<ProvinceResponse> {
        return RetrofiClient.getApiRegion().getProvince()
    }

    suspend fun getRegencies(id: String): List<RegenciesResponse> {
        return RetrofiClient.getApiRegion().getRegencies(id)
    }

    suspend fun getDistricts(id: String): List<DistrictResponse> {
        return RetrofiClient.getApiRegion().getDistrict(id)
    }

    suspend fun getVillages(id: String): List<VillageResponse> {
        return RetrofiClient.getApiRegion().getVillage(id)
    }

    suspend fun addUser(addUserRequest: AddUserRequest): SuccessResponse{
        return RetrofiClient.getApiArtSpace().addUser(addUserRequest)
        /*try {
            val response = RetrofiClient.getApiArtSpace().addUser(nama = name, email = email, deskripsi = bio, interest)
            if (response.isSuccessful) {
                UserResponse.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                UserResponse.Error(errorResponse.error, errorResponse.details)
            }
        } catch (e: HttpException) {
            UserResponse.Error("Error True", e.message())
        } catch (e: Exception) {
            UserResponse.Error("Error Exeption",e.message ?: "An unknown error occurred")
        }*/
    }

    companion object {
        @Volatile
        private var instance: ProvideRepository? = null
        fun getInstance(
            providePreference: ProvidePreference
        ): ProvideRepository =
            instance ?: synchronized(this) {
                instance ?: ProvideRepository(providePreference)
            }.also { instance = it }
    }
}
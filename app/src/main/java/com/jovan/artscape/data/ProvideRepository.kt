package com.jovan.artscape.data

import com.jovan.artscape.data.pref.ProvidePreference

class ProvideRepository private constructor(
){

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
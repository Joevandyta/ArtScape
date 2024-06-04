package com.jovan.artscape.data

import android.content.Context
import com.jovan.artscape.data.pref.ProvidePreference
import com.jovan.artscape.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): ProvideRepository {
        val pref = ProvidePreference.getInstance(context.dataStore)
        return ProvideRepository.getInstance(pref)
    }
}
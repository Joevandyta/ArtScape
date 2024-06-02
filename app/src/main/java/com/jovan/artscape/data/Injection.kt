package com.jovan.artscape.data

import android.content.Context

object Injection {
    fun provideRepository(context: Context): ProvideRepository {
        return ProvideRepository.getInstance()
    }
}
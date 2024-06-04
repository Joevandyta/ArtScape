package com.jovan.artscape.data.pref


class ProvidePreference private constructor() {




    companion object {
        @Volatile
        private var INSTANCE: ProvidePreference? = null

        fun getInstance(): ProvidePreference {
            return INSTANCE ?: synchronized(this) {
                val instance = ProvidePreference()
                INSTANCE = instance
                instance
            }
        }
    }
}
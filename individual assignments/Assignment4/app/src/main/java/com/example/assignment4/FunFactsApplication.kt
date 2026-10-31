package com.example.assignment4

import android.app.Application
import com.example.assignment4.data.FunFactApi
import com.example.assignment4.data.FunFactRepository
import com.example.assignment4.data.FunFactsDatabase

// These instances are shared by the whole app.
class FunFactsApplication : Application() {
    val database: FunFactsDatabase by lazy {
        FunFactsDatabase.getInstance(this)
    }

    val httpClient by lazy {
        FunFactApi.client
    }

    val repository: FunFactRepository by lazy {
        FunFactRepository.getInstance(database.funFactDao(), httpClient)
    }
}

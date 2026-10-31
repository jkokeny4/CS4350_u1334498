package com.example.assignment4.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

// A Kotlin object gives the app one shared Ktor client.
object FunFactApi {
    val client: HttpClient by lazy {
        HttpClient(Android) {
            expectSuccess = true
            defaultRequest {
                header(HttpHeaders.UserAgent, "FunFactsAssignment/1.0")
            }
        }
    }
}

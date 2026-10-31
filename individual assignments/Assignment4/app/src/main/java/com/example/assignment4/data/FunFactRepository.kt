package com.example.assignment4.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.json.JSONObject

// The ViewModel uses this repository for both saved facts and new facts.
class FunFactRepository private constructor(
    private val dao: FunFactDao,
    private val client: HttpClient
) {
    fun observeFacts(keyword: String): Flow<List<FunFact>> {
        return dao.observeFacts(keyword.trim())
    }

    suspend fun fetchAndSaveFact() {
        withContext(Dispatchers.IO) {
            val responseText = client.get(
                "https://uselessfacts.jsph.pl/random.json?language=en"
            ).body<String>()
            val apiFact = ApiFunFact(JSONObject(responseText).getString("text"))
            val fact = FunFact(text = apiFact.text, fetchedAt = System.currentTimeMillis())
            dao.insert(fact)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: FunFactRepository? = null

        fun getInstance(dao: FunFactDao, client: HttpClient): FunFactRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FunFactRepository(dao, client).also { INSTANCE = it }
            }
    }
}

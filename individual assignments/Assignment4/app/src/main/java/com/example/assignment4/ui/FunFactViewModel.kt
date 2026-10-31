package com.example.assignment4.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment4.data.FunFact
import com.example.assignment4.data.FunFactRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class FunFactViewModel(private val repository: FunFactRepository) : ViewModel() {
    private val searchQuery = MutableStateFlow("")
    val query: StateFlow<String> = searchQuery
    val facts: StateFlow<List<FunFact>> = searchQuery
        .flatMapLatest { repository.observeFacts(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun updateSearch(query: String) {
        searchQuery.value = query
    }

    fun fetchFact() {
        if (_isLoading.value) {
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                repository.fetchAndSaveFact()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value = "Could not fetch a fact. Check your connection and try again."
            } finally {
                _isLoading.value = false
            }
        }
    }
}

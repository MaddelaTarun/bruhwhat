package com.manekelsa.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manekelsa.app.data.FirebaseRepository
import com.manekelsa.app.model.WorkerProfile
import com.manekelsa.app.model.WorkerSkill
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * WorkerFeedViewModel — drives the Resident Discovery screen.
 * Exposes a filtered + sorted list of available workers.
 */
class WorkerFeedViewModel : ViewModel() {

    private val repository = FirebaseRepository()

    // Mock resident location (Bengaluru city center as default)
    private val residentLat = 13.0827
    private val residentLon = 77.5877

    // Raw stream from Firebase
    private val _allAvailableWorkers = MutableStateFlow<List<WorkerProfile>>(emptyList())

    // Active skill filter (null = show all)
    private val _selectedSkill = MutableStateFlow<WorkerSkill?>(null)
    val selectedSkill: StateFlow<WorkerSkill?> = _selectedSkill

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // Loading / error state
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Thumbs-up feedback (workerId -> true means animation triggered)
    private val _thumbsUpFeedback = MutableStateFlow<Set<String>>(emptySet())
    val thumbsUpFeedback: StateFlow<Set<String>> = _thumbsUpFeedback

    /**
     * Derived list: filtered by skill + search, sorted by distance.
     */
    val workers: StateFlow<List<WorkerProfile>> = combine(
        _allAvailableWorkers,
        _selectedSkill,
        _searchQuery
    ) { workers, skill, query ->
        workers
            .filter { worker ->
                val matchesSkill = skill == null || worker.skill == skill
                val matchesQuery = query.isBlank() ||
                        worker.name.contains(query, ignoreCase = true) ||
                        worker.area.contains(query, ignoreCase = true)
                matchesSkill && matchesQuery
            }
            .sortedBy { worker ->
                haversineDistance(residentLat, residentLon, worker.latitude, worker.longitude)
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadWorkers()
    }

    private fun loadWorkers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getAvailableWorkers().collect { list ->
                    _allAvailableWorkers.value = list
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = "ಡೇಟಾ ಲೋಡ್ ಆಗಲಿಲ್ಲ: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun setSkillFilter(skill: WorkerSkill?) {
        _selectedSkill.value = skill
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun giveThumbsUp(workerId: String) {
        // Optimistic UI: add to feedback set immediately
        _thumbsUpFeedback.value = _thumbsUpFeedback.value + workerId
        repository.addThumbsUp(workerId) { error ->
            if (error != null) {
                // Revert on failure
                _thumbsUpFeedback.value = _thumbsUpFeedback.value - workerId
            }
        }
        // Clear animation flag after delay
        viewModelScope.launch {
            kotlinx.coroutines.delay(1500)
            _thumbsUpFeedback.value = _thumbsUpFeedback.value - workerId
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    // ─── Haversine distance formula ───────────────────────────────────────────

    private fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        if (lat2 == 0.0 && lon2 == 0.0) return Double.MAX_VALUE // no location data
        val r = 6371.0 // Earth radius in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }
}

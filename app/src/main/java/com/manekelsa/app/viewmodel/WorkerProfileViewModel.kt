package com.manekelsa.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manekelsa.app.data.FirebaseRepository
import com.manekelsa.app.model.WorkerProfile
import com.manekelsa.app.model.WorkerSkill
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * WorkerProfileViewModel — manages the Worker's own profile and availability toggle.
 * In a real app this would be tied to Firebase Auth UID.
 * For this demo, we use a locally persisted worker ID (SharedPreferences / DataStore).
 */
class WorkerProfileViewModel : ViewModel() {

    private val repository = FirebaseRepository()

    private val _workerProfile = MutableStateFlow<WorkerProfile?>(null)
    val workerProfile: StateFlow<WorkerProfile?> = _workerProfile

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    private val _saveSuccess = MutableStateFlow<Boolean?>(null)
    val saveSuccess: StateFlow<Boolean?> = _saveSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Form fields
    val nameInput = MutableStateFlow("")
    val phoneInput = MutableStateFlow("")
    val areaInput = MutableStateFlow("")
    val dailyRateInput = MutableStateFlow("")
    val selectedSkill = MutableStateFlow(WorkerSkill.CLEANING)
    val isAvailable = MutableStateFlow(false)

    fun loadProfile(workerId: String) {
        viewModelScope.launch {
            val workers = repository.getAllWorkers().first()
            val profile = workers.find { it.id == workerId }
            profile?.let {
                _workerProfile.value = it
                nameInput.value = it.name
                phoneInput.value = it.phoneNumber
                areaInput.value = it.area
                dailyRateInput.value = it.dailyRate.toString()
                selectedSkill.value = it.skill
                isAvailable.value = it.isAvailable
            }
        }
    }

    fun saveProfile(existingId: String? = null) {
        val name = nameInput.value.trim()
        val phone = phoneInput.value.trim()
        val area = areaInput.value.trim()
        val rate = dailyRateInput.value.trim().toIntOrNull() ?: 0

        if (name.isBlank() || phone.isBlank() || area.isBlank()) {
            _errorMessage.value = "Please fill all information" // Will be localized in UI
            return
        }

        _isSaving.value = true
        val profile = WorkerProfile(
            id = existingId ?: "",
            name = name,
            skill = selectedSkill.value,
            phoneNumber = phone,
            area = area,
            dailyRate = rate,
            isAvailable = isAvailable.value
        )

        if (existingId == null) {
            repository.addWorker(profile) { id, error ->
                _isSaving.value = false
                if (error != null) {
                    _errorMessage.value = "Could not save: ${error.message}"
                    _saveSuccess.value = false
                } else {
                    _saveSuccess.value = true
                }
            }
        } else {
            repository.updateWorker(profile) { error ->
                _isSaving.value = false
                if (error != null) {
                    _errorMessage.value = "Could not update: ${error.message}"
                    _saveSuccess.value = false
                } else {
                    _saveSuccess.value = true
                }
            }
        }
    }

    fun toggleAvailability(workerId: String) {
        val newValue = !isAvailable.value
        isAvailable.value = newValue
        repository.setAvailability(workerId, newValue) { error ->
            if (error != null) {
                // Revert on failure
                isAvailable.value = !newValue
                _errorMessage.value = "Could not change status"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun clearSaveSuccess() {
        _saveSuccess.value = null
    }
}

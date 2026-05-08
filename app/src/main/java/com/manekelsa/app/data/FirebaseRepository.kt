package com.manekelsa.app.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.manekelsa.app.model.WorkerProfile
import com.manekelsa.app.model.WorkerSkill
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * FirebaseRepository — single source of truth for all Firebase Realtime Database operations.
 * Uses callbackFlow to expose real-time updates as Kotlin Flows.
 */
class FirebaseRepository {

    private val database = FirebaseDatabase.getInstance()
    private val workersRef = database.getReference("workers")

    // ─── Helpers: snapshot → model ───────────────────────────────────────────

    private fun snapshotToWorker(child: DataSnapshot): WorkerProfile? {
        val id = child.key ?: return null
        return try {
            WorkerProfile(
                id = id,
                name = child.child("name").getValue(String::class.java) ?: "",
                skill = WorkerSkill.valueOf(
                    child.child("skill").getValue(String::class.java) ?: "CLEANING"
                ),
                phoneNumber = child.child("phoneNumber").getValue(String::class.java) ?: "",
                area = child.child("area").getValue(String::class.java) ?: "",
                dailyRate = child.child("dailyRate").getValue(Int::class.java) ?: 0,
                photoUrl = child.child("photoUrl").getValue(String::class.java) ?: "",
                isAvailable = child.child("isAvailable").getValue(Boolean::class.java) ?: false,
                thumbsUp = child.child("thumbsUp").getValue(Int::class.java) ?: 0,
                latitude = child.child("latitude").getValue(Double::class.java) ?: 0.0,
                longitude = child.child("longitude").getValue(Double::class.java) ?: 0.0
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun listenToWorkers(
        filterAvailable: Boolean = false
    ): Flow<List<WorkerProfile>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val workers = snapshot.children.mapNotNull { child ->
                    snapshotToWorker(child)
                }.let { list ->
                    if (filterAvailable) list.filter { it.isAvailable } else list
                }
                trySend(workers)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        workersRef.addValueEventListener(listener)
        awaitClose { workersRef.removeEventListener(listener) }
    }

    // ─── Read: stream all available workers ──────────────────────────────────

    /**
     * Returns a Flow that emits available workers whenever Firebase data changes.
     */
    fun getAvailableWorkers(): Flow<List<WorkerProfile>> = listenToWorkers(filterAvailable = true)

    /**
     * Returns a Flow of ALL workers.
     */
    fun getAllWorkers(): Flow<List<WorkerProfile>> = listenToWorkers()

    // ─── Write: create / update worker ───────────────────────────────────────

    /**
     * Saves a new worker profile. Uses push() to auto-generate a unique ID.
     * Returns the generated worker ID on success.
     */
    fun addWorker(profile: WorkerProfile, onResult: (String?, Exception?) -> Unit) {
        val newRef = workersRef.push()
        val id = newRef.key ?: run {
            onResult(null, Exception("Firebase push key is null"))
            return
        }
        val data = profileToMap(profile.copy(id = id))
        newRef.setValue(data)
            .addOnSuccessListener { onResult(id, null) }
            .addOnFailureListener { onResult(null, it) }
    }

    /**
     * Updates an existing worker profile by ID.
     */
    fun updateWorker(profile: WorkerProfile, onResult: (Exception?) -> Unit) {
        workersRef.child(profile.id)
            .setValue(profileToMap(profile))
            .addOnSuccessListener { onResult(null) }
            .addOnFailureListener { onResult(it) }
    }

    // ─── Availability toggle ──────────────────────────────────────────────────

    /**
     * Flips the isAvailable flag for a worker. This is the core real-time sync operation.
     */
    fun setAvailability(workerId: String, isAvailable: Boolean, onResult: (Exception?) -> Unit) {
        workersRef.child(workerId).child("isAvailable")
            .setValue(isAvailable)
            .addOnSuccessListener { onResult(null) }
            .addOnFailureListener { onResult(it) }
    }

    // ─── Trust / rating ───────────────────────────────────────────────────────

    /**
     * Increments the thumbsUp counter atomically using Firebase transactions.
     */
    fun addThumbsUp(workerId: String, onResult: (Exception?) -> Unit) {
        val thumbsRef = workersRef.child(workerId).child("thumbsUp")
        thumbsRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                val current = mutableData.getValue(Int::class.java) ?: 0
                mutableData.value = current + 1
                return Transaction.success(mutableData)
            }

            override fun onComplete(error: DatabaseError?, committed: Boolean, data: DataSnapshot?) {
                if (error != null) {
                    onResult(error.toException())
                } else {
                    onResult(null)
                }
            }
        })
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private fun profileToMap(profile: WorkerProfile): Map<String, Any> = mapOf(
        "id" to profile.id,
        "name" to profile.name,
        "skill" to profile.skill.name,
        "phoneNumber" to profile.phoneNumber,
        "area" to profile.area,
        "dailyRate" to profile.dailyRate,
        "photoUrl" to profile.photoUrl,
        "isAvailable" to profile.isAvailable,
        "thumbsUp" to profile.thumbsUp,
        "latitude" to profile.latitude,
        "longitude" to profile.longitude
    )

    // ─── Seed mock data (dev only) ────────────────────────────────────────────

    /**
     * Seeds the database with sample workers for testing.
     * Call once from a debug build; remove before production.
     */
    fun seedMockData() {
        val mockWorkers = listOf(
            WorkerProfile(
                name = "ಸುಮಾ ರೆಡ್ಡಿ", skill = com.manekelsa.app.model.WorkerSkill.CLEANING,
                phoneNumber = "9876543210", area = "ಗಾಂಧಿ ನಗರ",
                dailyRate = 400, isAvailable = true, thumbsUp = 12,
                latitude = 13.0827, longitude = 77.5877
            ),
            WorkerProfile(
                name = "ರಾಜು ನಾಯ್ಕ", skill = com.manekelsa.app.model.WorkerSkill.GARDENING,
                phoneNumber = "9845012345", area = "ಜೆ.ಪಿ. ನಗರ",
                dailyRate = 350, isAvailable = true, thumbsUp = 8,
                latitude = 13.0900, longitude = 77.5800
            ),
            WorkerProfile(
                name = "ಲಕ್ಷ್ಮಿ ದೇವಿ", skill = com.manekelsa.app.model.WorkerSkill.COOKING,
                phoneNumber = "9731234567", area = "ಕೋರಮಂಗಲ",
                dailyRate = 500, isAvailable = false, thumbsUp = 20,
                latitude = 12.9352, longitude = 77.6245
            ),
            WorkerProfile(
                name = "ಮಹೇಶ್ ಕುಮಾರ್", skill = com.manekelsa.app.model.WorkerSkill.WASHING,
                phoneNumber = "9632587410", area = "ಇಂದಿರಾ ನಗರ",
                dailyRate = 300, isAvailable = true, thumbsUp = 5,
                latitude = 12.9784, longitude = 77.6408
            ),
            WorkerProfile(
                name = "ಪ್ರಭಾ ಶೆಟ್ಟಿ", skill = com.manekelsa.app.model.WorkerSkill.CLEANING,
                phoneNumber = "9512345678", area = "ಗಾಂಧಿ ನಗರ",
                dailyRate = 380, isAvailable = true, thumbsUp = 15,
                latitude = 13.0830, longitude = 77.5880
            )
        )
        mockWorkers.forEach { worker ->
            addWorker(worker) { _, _ -> }
        }
    }
}

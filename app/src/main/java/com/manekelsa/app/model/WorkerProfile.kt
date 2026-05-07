package com.manekelsa.app.model

/**
 * WorkerProfile — core data model stored in Firebase Realtime Database.
 * All fields have default values so Firebase can deserialize with no-arg constructor.
 */
data class WorkerProfile(
    val id: String = "",
    val name: String = "",
    val skill: WorkerSkill = WorkerSkill.CLEANING,
    val phoneNumber: String = "",
    val area: String = "",           // Street / locality name (e.g. "Gandhi Nagar")
    val dailyRate: Int = 0,          // Rate in INR per day
    val photoUrl: String = "",       // Firebase Storage URL or empty
    val isAvailable: Boolean = false,
    val thumbsUp: Int = 0,           // Cumulative positive ratings
    val latitude: Double = 0.0,      // For distance sorting (optional / mock)
    val longitude: Double = 0.0
)

enum class WorkerSkill(val kannada: String, val iconRes: String) {
    CLEANING("ಸ್ವಚ್ಛತೆ", "ic_broom"),
    GARDENING("ತೋಟಗಾರಿಕೆ", "ic_plant"),
    COOKING("ಅಡುಗೆ", "ic_cooking"),
    WASHING("ಬಟ್ಟೆ ತೊಳೆಯುವುದು", "ic_washing"),
    SECURITY("ಭದ್ರತೆ", "ic_security"),
    DRIVING("ಚಾಲನೆ", "ic_driving"),
    OTHER("ಇತರೆ", "ic_other")
}

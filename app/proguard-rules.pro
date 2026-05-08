# Firebase Realtime Database
-keepattributes Signature
-keepattributes *Annotation*
-keepclassmembers class com.manekelsa.app.model.** {
    *;
}
-keep class com.google.firebase.database.** { *; }

# Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Coil
-dontwarn coil.**

# Keep data models for Firebase serialization
-keep class com.manekelsa.app.data.** { *; }
-keep class com.manekelsa.app.model.WorkerProfile { *; }

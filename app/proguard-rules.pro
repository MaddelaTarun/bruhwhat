# Firebase Realtime Database
-keepattributes Signature
-keepattributes *Annotation*
-keepclassmembers class com.manekelsa.app.model.** {
    *;
}

# Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Coil
-dontwarn coil.**

# ─────────────────────────────────────────────────────
# Navigation2 type-safe routes (@Serializable data classes)
# ─────────────────────────────────────────────────────
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class ncshack.samba.mizan.navigation.**$$serializer { *; }
-keepclassmembers class ncshack.samba.mizan.navigation.** {
    *** Companion;
}
-keepclasseswithmembers class ncshack.samba.mizan.navigation.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# ─────────────────────────────────────────────────────
# Apollo Kotlin — generated operation & type classes
# ─────────────────────────────────────────────────────
-keep class ncshack.samba.mizan.type.** { *; }
-keepclassmembers class ncshack.samba.mizan.**Query { *; }
-keepclassmembers class ncshack.samba.mizan.**Mutation { *; }
-keepclassmembers class ncshack.samba.mizan.**Subscription { *; }
-dontwarn com.google.errorprone.annotations.CanIgnoreReturnValue
-dontwarn com.google.errorprone.annotations.CheckReturnValue
-dontwarn com.google.errorprone.annotations.Immutable
-dontwarn com.google.errorprone.annotations.RestrictedApi
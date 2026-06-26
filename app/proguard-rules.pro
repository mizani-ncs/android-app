# ─────────────────────────────────────────────────────
# Navigation2 type-safe routes (@Serializable data classes)
# ─────────────────────────────────────────────────────
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

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
-keep class com.apollographql.apollo.** { *; }
-dontwarn com.apollographql.apollo.**

# ─────────────────────────────────────────────────────
# Apollo Kotlin HTTP cache / normalized cache / SQLite
# ─────────────────────────────────────────────────────
-dontwarn com.apollographql.cache.**
-dontwarn com.apollographql.apollo.cache.normalized.**

# ─────────────────────────────────────────────────────
# Koin DI
# ─────────────────────────────────────────────────────
-keep class org.koin.** { *; }
-keepclassmembers class * {
    @org.koin.core.annotation.Single <fields>;
    @org.koin.core.annotation.Factory <fields>;
}

# ─────────────────────────────────────────────────────
# Room (DAOs, entities, database)
# ─────────────────────────────────────────────────────
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keepclassmembers class * {
    @androidx.room.Dao *;
    @androidx.room.Query *;
    @androidx.room.Insert *;
    @androidx.room.Update *;
    @androidx.room.Delete *;
}
-dontwarn androidx.room.paging.**

# ─────────────────────────────────────────────────────
# Coil (image loading)
# ─────────────────────────────────────────────────────
-keep class coil.** { *; }
-dontwarn coil.**

# ─────────────────────────────────────────────────────
# Material Kolor (DynamicMaterialTheme)
# ─────────────────────────────────────────────────────
-keep class com.materialkolor.** { *; }
-dontwarn com.materialkolor.**

# ─────────────────────────────────────────────────────
# Ktor client
# ─────────────────────────────────────────────────────
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

# ─────────────────────────────────────────────────────
# Kotlinx Serialization (general catch-all)
# ─────────────────────────────────────────────────────
-keep,includedescriptorclasses class kotlinx.serialization.**$$serializer { *; }
-keepclassmembers class kotlinx.serialization.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# ─────────────────────────────────────────────────────
# EncryptedSharedPreferences (AndroidX Security)
# ─────────────────────────────────────────────────────
-keep class androidx.security.crypto.** { *; }

# ─────────────────────────────────────────────────────
# KMPalette (dominant color extraction)
# ─────────────────────────────────────────────────────
-keep class com.kyant.kmpalette.** { *; }
-dontwarn com.kyant.kmpalette.**

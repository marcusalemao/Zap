// Pure Kotlin on purpose: no android.* imports live here, so the same rules can
// later run in a standalone glasses app or on a server.
plugins {
    id("com.android.library")
}

android {
    namespace = "io.alema.zap.core"
    compileSdk = 36
    defaultConfig { minSdk = 30 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
}

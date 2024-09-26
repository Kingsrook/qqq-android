plugins {
   alias(libs.plugins.android.library)
   alias(libs.plugins.kotlin.android)
   kotlin("plugin.serialization")
   id("kotlinx-serialization")
}

android {
   namespace = "com.kingsrook.qqq.frontend.android.core"
   compileSdk = 34

   defaultConfig {
      minSdk = 24

      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
      consumerProguardFiles("consumer-rules.pro")
   }

   buildTypes {
      release {
         isMinifyEnabled = false
         proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      }
   }
   compileOptions {
      sourceCompatibility = JavaVersion.VERSION_1_8
      targetCompatibility = JavaVersion.VERSION_1_8
   }
   kotlinOptions {
      jvmTarget = "1.8"
   }
}

dependencies {

   implementation(libs.androidx.core.ktx)
   implementation(libs.androidx.appcompat)
   implementation(libs.material)

   testImplementation(libs.junit)
   testImplementation(libs.junit.jupiter)
   androidTestImplementation(libs.androidx.junit)
   androidTestImplementation(libs.androidx.espresso.core)

   // Kotlin serialization
   implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

   // Retrofit
   implementation("com.squareup.retrofit2:retrofit:2.9.0")

   // Retrofit with Kotlin serialization Converter
   implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
   implementation("com.squareup.okhttp3:okhttp:4.11.0")
}
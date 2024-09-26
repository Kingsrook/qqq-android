plugins {
   alias(libs.plugins.android.library)
   alias(libs.plugins.kotlin.android)
}

/*
buildFeatures {
    compose = true
}
composeOptions {
    kotlinCompilerExtensionVersion = rootProject.extra["compose_version"] as String
}
 */

android {
   namespace = "com.kingsrook.qqq.frontend.android.mobileapp"
   compileSdk = 34

   defaultConfig {
      minSdk = 24

      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
      consumerProguardFiles("consumer-rules.pro")

      manifestPlaceholders["auth0Domain"] = "placeholder.domain"
      manifestPlaceholders["auth0Scheme"] = "placeholderscheme"
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
   buildFeatures {
      compose = true
   }
   composeOptions {
      kotlinCompilerExtensionVersion = "1.5.1"
   }
   kotlinOptions {
      jvmTarget = "1.8"
   }
}

dependencies {

   implementation(libs.androidx.core.ktx)
   implementation(libs.androidx.appcompat)
   implementation(libs.material)
   implementation(libs.androidx.runtime.android)
   implementation(libs.androidx.ui.android)
   implementation(libs.androidx.material3.android)
   implementation(libs.androidx.ui.tooling.preview.android)
   testImplementation(libs.junit)
   androidTestImplementation(libs.androidx.junit)
   androidTestImplementation(libs.androidx.espresso.core)

   implementation(project(":qqq:android:core"))
   implementation(libs.auth0)

   implementation(libs.kotlinx.serialization.json)
   implementation(libs.androidx.datastore.preferences)
   implementation(libs.androidx.datastore)
}
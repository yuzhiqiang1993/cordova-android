

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android") version "1.8.22"
    id("com.vanniktech.maven.publish") version "0.29.0"
}

apply(from = "cordova.gradle")

val cordovaConfig: Map<*, *> by project.extra

android {
    namespace = "org.apache.cordova"
    compileSdk = (cordovaConfig["COMPILE_SDK_VERSION"]?.toString()?.toInt()) ?: 34

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    defaultConfig {
        minSdk = (cordovaConfig["MIN_SDK_VERSION"]?.toString()?.toInt()) ?: 24
    }

    sourceSets {
        getByName("main") {
            java.srcDirs("src")
        }
    }

    // Include config.xml setup script
    // apply from: "cordova.gradle" // 已在顶部 apply

    // build-extras.gradle (如果需要支持 gradle 后缀)
    val extFile = file("build-extras.gradle")
    if (extFile.exists()) {
        apply(from = extFile)
    }
}

dependencies {
    implementation("androidx.annotation:annotation:1.5.0")
    implementation("androidx.appcompat:appcompat:${cordovaConfig["ANDROIDX_APP_COMPAT_VERSION"]}")
    implementation("androidx.webkit:webkit:${cordovaConfig["ANDROIDX_WEBKIT_VERSION"]}")
    implementation("androidx.core:core-splashscreen:${cordovaConfig["ANDROIDX_CORE_SPLASHSCREEN_VERSION"]}")
}

mavenPublishing {

    // 发布到 Maven Central
    publishToMavenCentral()
    // 显式启用签名
    signAllPublications()
}



plugins {
    id("com.android.library")
    id("com.vanniktech.maven.publish")
}

android {
    namespace = "org.apache.cordova"
    compileSdk = 34

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }



    defaultConfig {
        minSdk = 21
    }

    sourceSets {
        getByName("main") {
            java.srcDirs("src")
        }
    }

    // Include config.xml setup script
    // apply from: "cordova.gradle" // 已在顶部 apply


}

dependencies {
    implementation("androidx.annotation:annotation:1.5.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    // 使用稳定支持的版本
    implementation("androidx.webkit:webkit:1.3.0")
    implementation("androidx.core:core-splashscreen:1.0.0")
}

mavenPublishing {

    // 发布到 Maven Central
    publishToMavenCentral()
    // 显式启用签名
    signAllPublications()
}

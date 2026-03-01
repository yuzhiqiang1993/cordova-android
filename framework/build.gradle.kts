
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
}

dependencies {
    implementation("androidx.annotation:annotation:1.5.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.webkit:webkit:1.3.0")
    implementation("androidx.core:core-splashscreen:1.0.0")
}

mavenPublishing {

    publishToMavenCentral()
    
    val versionName = project.findProperty("VERSION_NAME")?.toString() ?: ""
    val isSnapshot = versionName.endsWith("SNAPSHOT", ignoreCase = true)
    if (!isSnapshot) {
        signAllPublications()
    }
}

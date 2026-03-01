
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

    // 发布到 Maven Central (0.35.0 会智能检测 SNAPSHOT 分发到 S01)
    publishToMavenCentral()
    
    // SNAPSHOT 版在新的 Central Portal 通常免去了强制签名，仅对 release 版执行
    val versionName = project.findProperty("VERSION_NAME")?.toString() ?: ""
    val isSnapshot = versionName.endsWith("SNAPSHOT", ignoreCase = true)
    if (!isSnapshot) {
        signAllPublications()
    }
}

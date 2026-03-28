plugins {
    alias(libs.plugins.spring.boot)
}

dependencies {
//    implementation(project(":virtual-lab-platform-users"))

    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.webmvc)
}
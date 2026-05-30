plugins {
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(project(":virtual-lab-platform-commons"))
    implementation(project(":virtual-lab-platform-users"))
    implementation(project(":virtual-lab-platform-authentication"))
    implementation(project(":virtual-lab-platform-instances"))


    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.webmvc)
    implementation(libs.spring.boot.starter.data.jpa)
    runtimeOnly(libs.postgresql)
}
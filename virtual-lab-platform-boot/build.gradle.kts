plugins {
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(project(":virtual-lab-platform-commons"))
    implementation(project(":virtual-lab-platform-users"))
    implementation(project(":virtual-lab-platform-authentication"))
    implementation(project(":virtual-lab-platform-instances"))

    implementation(libs.findbugs)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.webmvc)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    runtimeOnly(libs.postgresql)

    testImplementation(libs.junit)
    testImplementation(libs.assertj)
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
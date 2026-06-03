plugins {
    id("java-library")
}


dependencies {
    implementation(project(":virtual-lab-platform-commons"))
    implementation(project(":virtual-lab-platform-users"))

    implementation(libs.findbugs)
    implementation(libs.jakarta.persistence.api)
    implementation(libs.spring.context)
    implementation(libs.spring.core)
    implementation(libs.spring.data.jpa)
    implementation(libs.spring.security.core)
    implementation(libs.spring.web)
    implementation(libs.spring.boot.autoconfigure)
    implementation(libs.docker.client)
    implementation(libs.docker.client.httpclient5)
    implementation("org.springframework:spring-websocket")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")


    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    runtimeOnly(libs.h2)

    testImplementation(libs.junit)
    testImplementation(libs.assertj)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.junit.jupiter)
    testImplementation(platform(libs.spring.boot.bom))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc")
    testImplementation(project(":virtual-lab-platform-authentication"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
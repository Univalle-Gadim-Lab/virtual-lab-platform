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
    implementation(libs.docker.client)


    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    runtimeOnly(libs.h2)

    testImplementation(libs.junit)
    testImplementation(libs.assertj)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
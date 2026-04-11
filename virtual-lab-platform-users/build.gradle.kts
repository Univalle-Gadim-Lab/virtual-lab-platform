plugins {
    id("java-library")
}


dependencies {
    implementation(project(":virtual-lab-platform-commons"))

    implementation(libs.findbugs)
    implementation(libs.jakarta.persistence.api)
    implementation(libs.spring.context)
    implementation(libs.spring.core)
    implementation(libs.spring.data.jpa)
    implementation(libs.spring.security.core)
    implementation(libs.spring.web)


    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    runtimeOnly(libs.h2)

    testImplementation(libs.junit)
}
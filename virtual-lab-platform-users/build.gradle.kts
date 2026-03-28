plugins {
    id("java-library")
}


dependencies {
    implementation(libs.spring.context)
//    implementation(libs.spring.data.jpa)
    implementation(libs.spring.core)
//    implementation("org.springframework.data:spring-data-jpa")

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    runtimeOnly(libs.h2)

    testImplementation(libs.junit)
}
plugins {
    id("java-library")
}


dependencies {
    implementation(libs.bson)
    implementation(libs.spring.context)

    testImplementation(libs.junit)
    testImplementation(libs.assertj)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
plugins {
    id("java-library")
}


dependencies {
    implementation("org.springframework:spring-context")
//    implementation("org.springframework.data:spring-data-jpa")
    implementation("org.springframework:spring-web")
//    implementation("org.hibernate:hibernate-core")
//    runtimeOnly("com.h2database:h2")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.springframework:spring-test:6.1.0")
}

tasks.test {
    useJUnitPlatform()
}
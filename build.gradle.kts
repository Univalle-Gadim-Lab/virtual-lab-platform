plugins {
	kotlin("jvm") version "2.3.10"
	id("java")
	alias(libs.plugins.dependency.management)
	alias(libs.plugins.spring.boot) apply false
}

allprojects {
	group = "edu.univalle.gadim.virtual_lab_platform"
	version = "0.0.1-SNAPSHOT"

	apply(plugin = "java")

	java {
		toolchain {
			languageVersion = JavaLanguageVersion.of(21)
		}
	}

	dependencies {
		implementation(platform(rootProject.libs.spring.boot.bom))
	}

	repositories {
		mavenLocal()
		mavenCentral()
	}
}

subprojects {

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}
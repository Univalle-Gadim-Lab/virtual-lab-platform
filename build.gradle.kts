plugins {
	id("java")
}

allprojects {
	group = "edu.univalle.gadim.virtual_lab_platform"
	version = "0.0.1-SNAPSHOT"

	repositories {
		mavenCentral()
	}
}

subprojects {
	apply(plugin = "java")

	java {
		toolchain {
			languageVersion = JavaLanguageVersion.of(21)
		}
	}
}
plugins {
	id("java")
}

allprojects {
	group = "edu.univalle.gadim"
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
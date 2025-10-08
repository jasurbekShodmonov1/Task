import org.apache.tools.ant.filters.ReplaceTokens

plugins {
	java
	id("org.springframework.boot") version "3.5.3"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.github.spotbugs") version "6.0.8"
	id("com.diffplug.spotless") version "6.25.0"
}


group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation ("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.security:spring-security-oauth2-jose:6.4.4")
	implementation ("org.liquibase:liquibase-core")

	implementation("com.nimbusds:nimbus-jose-jwt:9.40")
	implementation ("org.immutables:builder:2.10.0")
	annotationProcessor ("org.immutables:builder:2.10.0")

	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")


	// https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${rootProject.extra.get("openApiVersion")}")
	implementation("org.mapstruct:mapstruct:${rootProject.extra.get("mapstructVersion")}")

	annotationProcessor("org.mapstruct:mapstruct-processor:${rootProject.extra.get("mapstructVersion")}")
}

dependencyManagement{
	imports {
		mavenBom("org.immutables:bom:2.10.1")
	}
}


spotbugs {
	toolVersion.set("${rootProject.extra.get("spotBugsVersion")}")
	excludeFilter.set(file("${project.rootDir}/findbugs-exclude.xml"))
}

spotless {
	java {
		// https://github.com/google/google-java-format/releases/latest
		googleJavaFormat("1.21.0")
	}
}

tasks {
	spotbugsMain {
		effort.set(com.github.spotbugs.snom.Effort.MAX)
		reports.create("html") {
			enabled = true
		}
	}

	spotbugsTest {
		ignoreFailures = true
		reportLevel.set(com.github.spotbugs.snom.Confidence.HIGH)
		effort.set(com.github.spotbugs.snom.Effort.MIN)
		reports.create("html") {
			enabled = true
		}
	}
}



tasks.compileJava {
	dependsOn("processResources")
	options.release.set(21)
	options.encoding = "UTF-8"
	options.compilerArgs.addAll(listOf("-Xlint:deprecation"))
}

tasks.processResources {
	val tokens = mapOf(
		"application.version" to project.version,
		"application.description" to project.description
	)
	filesMatching("**/*.yml") {
		filter<ReplaceTokens>("tokens" to tokens)
	}
}

tasks.test {
	failFast = false
	enableAssertions = true

	// Enable JUnit 5 (Gradle 4.6+).
	useJUnitPlatform()

	testLogging {
		events("PASSED", "STARTED", "FAILED", "SKIPPED")
		// Set to true if you want to see output from tests
		showStandardStreams = false
		setExceptionFormat("FULL")
	}

	systemProperty("io.netty.leakDetectionLevel", "paranoid")
}
tasks.withType<Test> {
	useJUnitPlatform()
}

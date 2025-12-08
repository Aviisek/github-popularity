
plugins {
	java
	id("org.springframework.boot") version "4.0.0"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.redcare"
version = "0.0.1"
description = "Redcare coding challenge"



java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.13")
    implementation("org.apache.httpcomponents.client5:httpclient5")

    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.assertj:assertj-core:3.25.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("com.squareup.okhttp3:mockwebserver3-junit5:5.3.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.bootJar {
    archiveBaseName.set("github-popularity")
    archiveVersion.set(version.toString())
}

tasks.test {
    jvmArgs = listOf("-XX:UseSVE=0","-Xshare:off")
}

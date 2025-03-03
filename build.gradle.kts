import org.apache.tools.ant.filters.ReplaceTokens

val javaVersion = 23
val env = project.property("env") ?: "prod"

plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.boot.dependency.management)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.spring)
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))

repositories {
    mavenCentral()
}

allOpen {
    annotation("org.springframework.stereotype.Component")
    annotation("org.springframework.stereotype.Service")
    annotation("org.springframework.stereotype.Repository")
    annotation("org.springframework.stereotype.Controller")
    annotation("org.springframework.context.annotation.Configuration")
}

dependencies {
    // Spring Framework
    implementation("org.springframework.boot", "spring-boot-starter-data-redis")
    implementation("org.springframework.boot", "spring-boot-starter-web")
    implementation("org.springframework.boot", "spring-boot-starter-validation")
    implementation("org.springframework.boot", "spring-boot-starter-test")
    compileOnly("org.springframework.boot", "spring-boot-configuration-processor")

    // Mybatis-plus
    implementation(libs.bundles.mp)

    // PostgreSQL
    runtimeOnly("org.postgresql", "postgresql")

    // JUnit Test
    testRuntimeOnly("org.junit.platform", "junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.processResources {
    filesMatching("application.yml") {
        filter<ReplaceTokens>("tokens" to mapOf("env" to env))
    }
}

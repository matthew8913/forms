plugins {
    java
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "edu.eltex"
version = "0.1"

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
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.springframework.security:spring-security-test")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.liquibase:liquibase-core")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register("buildUI") {
    group = "custom"
    description = "Build UI and copy into static resources"

    val uiFolder by extra { file("ui") }

    doLast {
        if (!uiFolder.exists()) {
            throw GradleException("Folder 'ui' not found. Please make sure the 'ui' directory exists.")
        }
        project.providers.exec() {
            workingDir = uiFolder
            commandLine("npm", "run", "build")
        }
    }

    finalizedBy("copyToResources")
    println("[GRADLE TASKS] - UI built and copied successfully.")
}

tasks.register("copyToResources") {
    group = "custom"
    description = "Copy data"

    dependsOn("buildUI")

    doLast {
        val uiDist = file("ui/dist")
        val resourcesStatic = file("src/main/resources/static")

        if (!resourcesStatic.exists()) {
            resourcesStatic.mkdirs()
        }

        project.copy {
            from(uiDist)
            into(resourcesStatic)
        }
    }
}

tasks.findByName("bootRun")?.dependsOn("buildUI")
tasks.findByName("bootJar")?.dependsOn("buildUI")

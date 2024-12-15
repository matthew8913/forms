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

    implementation("org.apache.poi:poi-ooxml:5.2.3")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register("buildUI") {
    group = "custom"
    description = "Build UI and copy into static resources"

    val distFolder by extra { file("ui/dist") }
    val uiFolder by extra { file("ui") }
    val env = file(".env")

    doLast {
        if (!env.exists()) {
            throw GradleException("No .env in root folder")
        } else if (!uiFolder.exists()) {
            throw GradleException("No ui/ in root folder")
        }

        if (distFolder.exists()) {
            println("[GRADLE TASKS] - ui/dist folder exist. Deleting...")
            delete(distFolder)
        }

        project.exec() {
            workingDir = uiFolder
            commandLine("npm", "install")
        }

        project.exec() {
            workingDir = uiFolder
            commandLine("npm", "fund")
        }

        project.exec() {
            workingDir = uiFolder
            commandLine("npm", "run", "build")
        }

        if (!distFolder.exists()) {
            throw GradleException("Folder 'ui/dist' did not created. UI build problem.")
        }

        println("[GRADLE TASKS] - UI built successfully.")
    }


    finalizedBy("copyToResources")
}

tasks.register("copyToResources") {
    group = "custom"
    description = "Copy data"

    dependsOn("buildUI")

    doLast {
        val uiDist = file("ui/dist")
        val resourcesStatic = file("src/main/resources/static")

        if (!resourcesStatic.exists()) {
            println("[GRADLE TASKS] - resources/static folder does not exist. Creating...")
            resourcesStatic.mkdirs()
        } else {
            println("[GRADLE TASKS] - resources/static folder exist. Clearing...")
            delete(resourcesStatic)
            resourcesStatic.mkdirs()
        }

        project.copy {
            from(uiDist)
            into(resourcesStatic)
        }

        println("[GRADLE TASKS] - UI dist copied successfully.")
    }
}

tasks.findByName("bootRun")?.dependsOn("buildUI")

tasks.register("depsize") {
    description = "Prints dependencies for \"default\" configuration"
    doLast {
        configurations["default"].isCanBeResolved = true
        listConfigurationDependencies(configurations["default"])
    }
}

tasks.register("depsize-all-configurations") {
    description = "Prints dependencies for all available configurations"
    doLast {
        configurations
                .filter { it.isCanBeResolved }
                .forEach { listConfigurationDependencies(it) }
    }
}

fun listConfigurationDependencies(configuration: Configuration ) {
    val formatStr = "%,10.2f"

    val size = configuration.map { it.length() / (1024.0 * 1024.0) }.sum()

    val out = StringBuffer()
    out.append("\nConfiguration name: \"${configuration.name}\"\n")
    if (size > 0) {
        out.append("Total dependencies size:".padEnd(65))
        out.append("${String.format(formatStr, size)} Mb\n\n")

        configuration.sortedBy { -it.length() }
                .forEach {
                    out.append(it.name.padEnd(65))
                    out.append("${String.format(formatStr, (it.length() / 1024.0))} kb\n")
                }
    } else {
        out.append("No dependencies found")
    }
    println(out)
}
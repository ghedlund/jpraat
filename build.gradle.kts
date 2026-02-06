plugins {
    `java-library`
    `maven-publish`
}

group = "ca.hedlund"
version = "1.2.2"

val libjpraatVersion = "6.3.02"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    withSourcesJar()
    withJavadocJar()
    modularity.inferModulePath = true
}

repositories {
    mavenCentral()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/ghedlund/jpraat")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    api("net.java.dev.jna:jna:5.12.1")

    runtimeOnly("ca.hedlund:libjpraat-win64:$libjpraatVersion")
    runtimeOnly("ca.hedlund:libjpraat-darwin:$libjpraatVersion")

    testImplementation("junit:junit:4.13.1")
}

tasks.test {
    forkEvery = 1
    maxParallelForks = 1
}

tasks.javadoc {
    isFailOnError = false
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/ghedlund/jpraat")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

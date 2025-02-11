import com.google.protobuf.gradle.*

val javaVersion = "1.8"
val protobufVersion = "3.14.0"
val grpcVersion = "1.35.0"
val grpcKotlinVersion = "1.0.0"

plugins {
  kotlin("jvm") version "1.4.21"
  id("com.google.protobuf") version "0.8.14"
  java
  application
}

application {
  group = "org.mvnsearch"
  version = "1.0.0-SNAPSHOT"
  mainClassName = "org.mvnsearch.greeter.GreeterServerKt"
}

repositories {
  maven("https://dl.bintray.com/kotlin/kotlin-eap")
  google()
  jcenter()
  mavenCentral()
  mavenLocal()
}

sourceSets {
  main {
    java {
      setSrcDirs(listOf("build/generated/source/proto/main/grpc", "build/generated/source/proto/main/java"))
    }
    withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
      kotlin.srcDir("src/main/kotlin")
      kotlin.srcDir("build/generated/source/proto/main/grpckt")
    }
  }
}


dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation("javax.annotation:javax.annotation-api:1.3.2")
  implementation("io.grpc:grpc-kotlin-stub:${grpcKotlinVersion}")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
  implementation("com.google.protobuf:protobuf-java:${protobufVersion}")
  implementation("com.google.protobuf:protobuf-java-util:${protobufVersion}")
  implementation("io.grpc:grpc-netty-shaded:${grpcVersion}")
  implementation("io.grpc:grpc-protobuf:${grpcVersion}")
  implementation("io.grpc:grpc-stub:${grpcVersion}")
  implementation("io.grpc:grpc-services:${grpcVersion}")
  implementation("com.google.guava:guava:30.1-jre")

  testImplementation(kotlin("test-junit5"))
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
}

java {
  sourceCompatibility = JavaVersion.toVersion(javaVersion)
  targetCompatibility = JavaVersion.toVersion(javaVersion)
}

tasks {
  compileKotlin {
    kotlinOptions.jvmTarget = javaVersion
  }
  compileTestKotlin {
    kotlinOptions.jvmTarget = javaVersion
  }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
  kotlinOptions.suppressWarnings = true
}

tasks.withType<Test> {
  useJUnitPlatform()
}

protobuf {

  protoc {
    // The artifact spec for the Protobuf Compiler
    artifact = "com.google.protobuf:protoc:${protobufVersion}"
  }

  plugins {
    id("grpc") {
      artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"
    }
    // Specify protoc to generate using our grpc kotlin plugin
    id("grpckt") {
      artifact = "io.grpc:protoc-gen-grpc-kotlin:${grpcKotlinVersion}:jdk7@jar"
    }
  }

  generateProtoTasks {
    ofSourceSet("main").forEach {
      it.plugins {
        id("grpc")
        id("grpckt")
      }
    }
  }
}

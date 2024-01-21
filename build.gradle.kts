group = "com.mk.sll"
version = "0.0.1-SNAPSHOT"
description = "sorted-linked-list"
java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

plugins {
    java
}


repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.assertj:assertj-core:3.25.1")
}


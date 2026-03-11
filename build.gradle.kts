plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.graphics", "javafx.base")
}


dependencies {

    implementation("com.opencsv:opencsv:5.9")
    implementation("org.controlsfx:controlsfx:11.2.1")
    implementation("com.dlsc.formsfx:formsfx-core:11.6.0")
    implementation("net.synedra:validatorfx:0.5.0")
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    implementation("org.kordamp.bootstrapfx:bootstrapfx-core:0.4.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass.set("org.example._555laba555.HelloApplication")
}

tasks.withType<JavaExec> {
    jvmArgs = listOf(
        "--module-path", sourceSets.main.get().runtimeClasspath.asPath,
        "--add-modules", "javafx.controls,javafx.fxml,javafx.graphics"
    )
}

tasks.test {
    useJUnitPlatform()
    jvmArgs = listOf(
        "--module-path", sourceSets.main.get().runtimeClasspath.asPath,
        "--add-modules", "javafx.controls,javafx.fxml,javafx.graphics"
    )
}
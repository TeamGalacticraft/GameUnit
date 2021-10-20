![Maven metadata URL](https://img.shields.io/maven-metadata/v?logo=apache-maven&metadataUrl=https%3A%2F%2Fmaven.galacticraft.dev%2Fdev%2Fgalacticraft%2FGameUnit%2Fmaven-metadata.xml&style=flat-square)
# GameUnit
A small utility mod for saving gametest results in the JUnit5 XML format.

## Usage
Just add the following to your `build.gradle`
```groovy
repositories {
    maven {
        url "https://maven.galacticraft.dev/"
    }
}

dependencies {
    modRuntime "dev.galacticraft:GameUnit:$gameunitVersion"
}
```

<details>
  <summary>Gradle Kotlin DSL</summary>

If you're using a Kotlin DSL just add the following to your `build.gradle.kts`.
```kotlin
repositories {
    maven("https://maven.galacticraft.dev/")
}

dependencies {
    modRuntime("dev.galacticraft:GameUnit:$gameunitVersion")
}
```
</details>

And that's it.\
When you run your gametest server task the results will be available at `run/TEST-gameunit.xml`.
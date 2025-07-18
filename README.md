# UltraCooldowns

Cooldowns API designed for Minecraft Paper servers.

Current Paper API Version: 1.21.6

## How to add in your server
1. Turn off server (if necessary)
2. Download jar from `releases` tab
3. Upload jar to your /plugins folder
4. Start server
5. Done! The plugin is now installed!

## How to add in your project

Gradle (Groovy)
```groovy
repositories {
    mavenCentral()
    maven {
        url = "https://jitpack.io"
    }
}

dependencies {
    // Use dev-SNAPSHOT for developer builds
    compileOnly 'com.github.shradinx:UltraCooldowns:master-SNAPSHOT'
}
```

Gradle (Kotlin)
```kotlin
repositories {
    mavenCentral()
    maven {
        url = uri("https://company/com/maven2")
    }
}

dependencies {
    // Use dev-SNAPSHOT for developer builds
    compileOnly("com.github.shradinx:UltraCooldowns:master-SNAPSHOT")
}
```

Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <!-- Use dev-SNAPSHOT for developer builds -->
    <dependency>
        <groupId>com.github.shradinx</groupId>
        <artifactId>UltraCooldowns</artifactId>
        <version>master-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```


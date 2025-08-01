<img src="public/image/lophine/lophine3.png" alt="Logo" align="right" width="250">

# Lophine

<h4>Lophine is a Luminol fork with many useful optimizations and configurable vanilla features, aims to provide
more function for survival-usable circuit on folia</h4>

[![License](https://img.shields.io/github/license/LuminolMC/Lophine?style=flat-square)](LICENSE.md)
[![Issues](https://img.shields.io/github/issues/LuminolMC/Lophine?style=flat-square)](https://github.com/LuminolMC/Lophine/issues)
![Commit Activity](https://img.shields.io/github/commit-activity/w/LuminolMC/Lophine?style=flat-square)
![CodeFactor Grade](https://img.shields.io/codefactor/grade/github/LuminolMC/Lophine?style=flat-square)
![GitHub all releases](https://img.shields.io/github/downloads/LuminolMC/Lophine/total?style=flat-square)

**English** | [中文](./README.md)

## Features

- Supported some Bukkit plugin
- Configurable vanilla features
- Tpsbar support
- Useful optimizations to improve the performance of single threaded region
- More functions

## Download

Any versions are available in the [release](https://github.com/LuminolMC/Lophine/releases), also you can build it by yourself through [the following steps](./README_EN.md#Build).

## Build

To build a paperclip jar, you need to run the following command. You can find the jar in lophine-server/build/libs(Note: `JDK21` is needed)

 ```shell
 ./gradlew applyAllPatches && ./gradlew createMojmapPaperclipJar
```

## Using API

For gradle:

```kotlin
repositories {
    maven {
        url = "https://repo.menthamc.com/repository/maven-public/"
    }
}

dependencies {
    compileOnly("me.earthme.luminol:luminol-api:$VERSION")
}
 ```

For maven

```xml
<repositories>
    <repository>
        <id>menthamc</id>
        <url>https://repo.menthamc.com/repository/maven-public/</url>
    </repository>
</repositories>

<dependencies>
<dependency>
    <groupId>me.earthme.luminol</groupId>
    <artifactId>luminol-api</artifactId>
    <version>$VERSION</version>
</dependency>
</dependencies>
```

## Contact

> If you are interested in this project or have any issue, feel free to ask us.

**QQ Group: [1015048616](http://qm.qq.com/cgi-bin/qm/qr?_wv=1027&k=hTPlI5j6XB8pgk4sdx6RkjhBPGG1r4IR&authKey=pnu6uCKQP7Sja2CJWC15Qi3BeI%2FAsh8tU4m5muufMBjbB3zz%2BwHBZCTRRdSNKhld&noverify=0&group_code=1015048616)** | QQ Channel: [Click To Join](https://pd.qq.com/s/eq9krf9j) | Telegram: [Click To Join](https://t.me/LuminolMinecraft) | Discord: [Click To Join](https://discord.gg/Qd7m3V6eDx)

## About Issue

When you meet any problems, just ask us, we will do our best to solve it, but remember to state your problem clear and provide enough logs etc.</br>

## Pull Requests

See [Contributing](./docs/CONTRIBUTING_EN.md)

## BStats

![bStats](https://bstats.org/signatures/server-implementation/Lophine.svg "bStats")

## Special Thanks

Thanks [LegacyLands](https://github.com/LegacyLands) for sponsoring this project.If you want to develop some crossing folia/non-folia platform plugins, [legacy-lands-library](https://github.com/LegacyLands/legacy-lands-library/) will be a nice choice for you

![legacy-lands-logo](public/image/legacy-lands-logo.png)

## Please ⭐star us!

<a href="https://star-history.com/#LuminolMC/Luminol&LuminolMC/LightingLuminol&LuminolMC/Lophine&Date">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="https://api.star-history.com/svg?repos=LuminolMC/Luminol%2CLuminolMC/LightingLuminol%2CLuminolMC/Lophine&type=Date&theme=dark" />
    <source media="(prefers-color-scheme: light)" srcset="https://api.star-history.com/svg?repos=LuminolMC/Luminol%2CLuminolMC/LightingLuminol%2CLuminolMC/Lophine&type=Date" />
    <img alt="Star History Chart" src="https://api.star-history.com/svg?repos=LuminolMC/Luminol%2CLuminolMC/LightingLuminol%2CLuminolMC/Lophine&type=Date" />
  </picture>
</a>

<img src="public/image/lophine/lophine3.png" alt="Logo" align="right" width="250">

# Lophine

<h4>Lophine 是一个基于Luminol的分支，具有许多有用的优化和可配置的原版特性，目标是在Folia上实现更多生电的实现</h4>

![Created At](https://img.shields.io/github/created-at/LuminolMC/Lophine?style=flat-square)
[![License](https://img.shields.io/github/license/LuminolMC/Lophine?style=flat-square)](LICENSE.md)
[![Issues](https://img.shields.io/github/issues/LuminolMC/Lophine?style=flat-square)](https://github.com/LuminolMC/Lophine/issues)
![Commit Activity](https://img.shields.io/github/commit-activity/w/LuminolMC/Lophine?style=flat-square)
![CodeFactor Grade](https://img.shields.io/codefactor/grade/github/LuminolMC/Lophine?style=flat-square)
![GitHub all releases](https://img.shields.io/github/downloads/LuminolMC/Lophine/total?style=flat-square)

[English](./README_EN.md) | **中文**

## 特性

- 支持部分Bukkit插件
- 可配置的原版特性
- Tpsbar 支持
- 针对folia bug的一些修复
- linear和b_linear(linear的重新实现)存档格式支持
- 对单线程区域性能的优化
- 更多有用的功能

## 下载

任何版本都可以在 [Release](https://github.com/LuminolMC/Lophine/releases), 中找到，你也可以通过[以下步骤](./README.md#构建)自己构建。

## 构建

要构建一个paperclip jar，你需要运行以下命令。你可以在lophine-server/build/libs中找到jar（注意：需要`JDK21`）

 ```shell
./gradlew applyAllPatches && ./gradlew createMojmapPaperclipJar
```

## 使用API

使用 Gradle:

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

使用 Maven

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

## 联系方式

> 如果您对这个项目感兴趣或有任何问题，请随时向我们提问。

**QQ群: [1015048616](http://qm.qq.com/cgi-bin/qm/qr?_wv=1027&k=QML5kIVsniPi1PlZvnjHQT_02EHsZ5Jc&authKey=%2FTCJsZC7JFQ9sxAroPCKuYnlV57Z5fyqp36ewXZk3Sn4iJ9p4MB1JKdc%2FFcX3HOM&noverify=0&group_code=1015048616)** | QQ频道: [点击加入](https://pd.qq.com/s/eq9krf9j) | Telegram: [点击加入](https://t.me/LuminolMinecraft) | Discord: [点击加入](https://discord.gg/Qd7m3V6eDx)


## 关于 Issue

当您遇到任何问题时，请向我们提问，我们将尽力解决，但请记得清楚地描述您的问题并提供足够的日志等信息。

## 贡献代码

可查看 [Contributing](./docs/CONTRIBUTING.md)

## BStats

![bStats](https://bstats.org/signatures/server-implementation/Lophine.svg "bStats")

## 特别感谢

<b>感谢[LegacyLands](https://github.com/LegacyLands)对本项目的赞助,如果你想开发一些跨folia/非folia平台的插件,[legacy-lands-library](https://github.com/LegacyLands/legacy-lands-library/)将会是个不错的lib</b>

![legacy-lands-logo](public/image/legacy-lands-logo.png)

## 请给我们一个 ⭐Star！

> 你的每一个免费的 ⭐Star 就是我们每一个前进的动力。

<a href="https://star-history.com/#LuminolMC/Luminol&LuminolMC/LightingLuminol&LuminolMC/Lophine&Date">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="https://api.star-history.com/svg?repos=LuminolMC/Luminol%2CLuminolMC/LightingLuminol%2CLuminolMC/Lophine&type=Date&theme=dark" />
    <source media="(prefers-color-scheme: light)" srcset="https://api.star-history.com/svg?repos=LuminolMC/Luminol%2CLuminolMC/LightingLuminol%2CLuminolMC/Lophine&type=Date" />
    <img alt="Star历史表" src="https://api.star-history.com/svg?repos=LuminolMC/Luminol%2CLuminolMC/LightingLuminol%2CLuminolMC/Lophine&type=Date" />
  </picture>
</a>

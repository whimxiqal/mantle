# Mantle

![Test and Build](https://github.com/pietelite/mantle/actions/workflows/build.yml/badge.svg)
[![Checkstyle](https://github.com/pietelite/mantle/actions/workflows/checkstyle.yml/badge.svg)](https://checkstyle.org/)
[![CodeQL](https://github.com/pietelite/mantle/actions/workflows/codeql.yml/badge.svg)](https://docs.github.com/en/code-security/code-scanning/automatically-scanning-your-code-for-vulnerabilities-and-errors/about-code-scanning)
![License](https://img.shields.io/github/license/pietelite/mantle)
![Maven Central](https://img.shields.io/maven-central/v/me.pietelite.mantle/common)

Mantle is a [Minecraft](https://www.minecraft.net) Command Framework that leverages [ANTLR4](https://github.com/antlr/antlr4) to automatically generate necessary command executor files, cutting down total code and vastly improving maintainability.

Mantle accepts a single ANTLR4 `.g4` grammar file and generates a series of files called parsers, lexers, and visitors. Together, these programmatically represent your entire command structure and gives you a single interface to implement all the functionality of your individual commands. Mantle also includes many other features found in other Minecraft command frameworks.

Read the [wiki](https://github.com/pietelite/mantle/wiki) for more information.
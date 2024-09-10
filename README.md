# Mantle

![Test + Build](https://github.com/whimxiqal/mantle/actions/workflows/build.yml/badge.svg?branch=main)
![License](https://img.shields.io/github/license/whimxiqal/mantle)
![Maven Central](https://img.shields.io/maven-central/v/net.whimxiqal.mantle/common?color=green)

Mantle is a [Minecraft](https://www.minecraft.net) Command Framework that leverages [ANTLR4](https://github.com/antlr/antlr4) to automatically generate necessary command executor files, cutting down total code and vastly improving maintainability.

Mantle accepts a single ANTLR4 `.g4` grammar file and generates a series of files called parsers, lexers, and visitors. Together, these programmatically represent your entire command structure and gives you a single interface to implement all the functionality of your individual commands. Mantle also includes many other features found in other Minecraft command frameworks.

Read the [wiki](https://github.com/whimxiqal/mantle/wiki) for more information.
# Mantle
Mantle is a [Minecraft](https://www.minecraft.net) Command Framework that leverages
[ANTLR4](https://github.com/antlr/antlr4) to automagically generate necessary command executor files.

Command frameworks generally take the approach of compiling a tree of nodes where various terms or parameters
are nodes in the tree. You, as the developer of a Minecraft plugin, must programmatically build this tree
from the ground up. Also, a tree pattern isn't always suitable for the type of command structure you may want for your plugin.

Mantle, combined with ANTLR4, accepts a single ANTLR4 `.g4` grammar file, generates a series of files called
parsers, lexers, and visitors, which essentially together programmatically represent your command structure and
gives you a single interface to implement all the functionality of your individual commands. 
Mantle also allows you to set up help info about commands for users in-game and command completion for unique parameters.

It only takes **three files**:
1. your grammar file
2. your command executor file
3. your Mantle command connector

Read the [wiki](https://github.com/pietelite/mantle/wiki) for more information.
# **Nebulune**

A **QoL addon for Athen**, featuring additional features and functionality beyond standard QoL.

NOTE: You will need Athen installed for Nebulune to work.
Get Athen at: https://modrinth.com/mod/athen

This maintained fork targets Minecraft **26.1.2, 26.2, and 26.3** with the
matching official **Athen 0.3.5** jar. Download the Minecraft-specific Nebulune
jar from [GitHub Releases](https://github.com/Hypixel-Skyblock-Mods/Nebulune/releases).
Fabric API and Fabric Language Kotlin are also required. Use Java 25.

---

## Building

With JDK 25 installed, run:

```sh
./gradlew :26.1:buildAndCollect :26.2:buildAndCollect :26.3:buildAndCollect
```

The `26.1` source target builds against Minecraft 26.1.2. Jars are collected in
`build/libs/0.3.1`. Builds include bytecode checks for mixin targets, shadowed
members, and injection points against the official dependencies. These checks
do not replace testing the features in a running game.

## Maintenance

This fork removes inherited GitHub Actions workflows and publishes releases
manually. Its update notifier checks this repository.

Thanks to [Gaeritag's fork](https://github.com/Gaeritag/Nebulune) for identifying
useful fishing ownership, NPC dialogue, rat entity detection, and terminal API
fixes. The relevant source changes were reviewed and adapted here in new commits;
that fork's commit history and bundled Athen beta binaries were not imported.

---

## Features

### Dungeons

* Auto superboom
* Auto terminals
* Breaker helper: zero ping db, prevent breaking secrets
* Chest closer
* Hover terminals
* Queue terminals

### General

* Auto experiments
* Etherwarp helper: left click etherwarp
* Fishing helper
* Trevor helper: auto call, auto accept, and pelt esp
* Wardrobe helper: auto close, auto equip, and auto equip while moving

### Kuudra

* Kuudra ESP
* Kuudra Peek alert
* Stun helper: auto close shop gui

### Render

* Camera helper: camera clip and custom camera distance
* Hideon ESP
* Pest ESP
* Rat ESP

### Slayers

* Auto soulcry
* Boss ESP
* Dagger swapper

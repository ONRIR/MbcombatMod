# Bannerlord Combat Mod - Build Instructions

This mod requires proper compilation with NeoForge toolchain.

## Prerequisites
- Java 21 JDK
- Gradle 8.7+
- Internet connection for dependency download

## Build Steps

1. Navigate to mod directory:
```bash
cd bannerlordcombat-mod
```

2. Run Gradle build:
```bash
./gradlew build
```

3. Find compiled mod at:
```
build/libs/bannerlordcombat-1.0.0.jar
```

## Installation
Place the compiled JAR in your Minecraft `mods` folder.

## Controls
- Arrow Keys (←→↑↓): Attack directions (Left, Right, Up, Thrust)
- Left Shift + Arrow Key: Block in that direction
- Release Shift: Stop blocking

## System Overview
- Directional melee combat system
- Manual blocking with direction matching
- Attack chaining with timing windows
- Mounted combat with speed-based damage
- No random damage or critical hits

# Escape Viewer Plugin for JetBrains IDEs

**One-click toggle** between rendered characters and raw escape sequences in string literals.

## Features
- Toggle button in toolbar + `Ctrl+Shift+E`
- Status bar indicator (RENDERED / RAW)
- Shows `<A68>` for `\x68` and `<U00E4>` for `\u00E4` in raw mode
- Works in Java, Kotlin, Python, JavaScript, and more
- Persistent settings
- Live update as you type

## Build from Command Line (Recommended for immediate import)

**Requirements:**
- Gradle 9.x (required by IntelliJ Platform Gradle Plugin 2.x)
- **Java 17 or later** (set `JAVA_HOME` to JDK 17+)
- Uses `org.jetbrains.intellij.platform` 2.16.0+ for building against modern IntelliJ SDKs

```bash
# 1. Check Java version (must be 17+)
java -version

# 2. Set JAVA_HOME if needed (example for macOS/Linux):
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home

# Windows example:
# set JAVA_HOME=C:\Program Files\Java\jdk-17

# 3. Navigate to the project folder
cd escape-viewer-plugin

# 4. Build the plugin
gradle clean buildPlugin

# 5. Plugin zip location:
#    build/distributions/escape-viewer-plugin-1.0.0.zip

# 6. Install in IntelliJ:
#    Settings → Plugins → ⚙️ → Install Plugin from Disk
```

## Quick Install (One Command)
```bash
gradle clean buildPlugin && echo "Plugin ready at: build/distributions/escape-viewer-plugin-1.0.0.zip"
```

## Installation via IDE (Alternative)
1. Open the folder in IntelliJ IDEA
2. Let it import the Gradle project
3. Run the `buildPlugin` task from the Gradle tool window
4. Install the generated zip

## Usage
1. Open any file with string literals containing `\x` or `\u`
2. Press `Ctrl+Shift+E` or click the toolbar button
3. Watch escapes turn into `<AXX>` / `<UXXXX>` or back to real characters

## Architecture
- Pure visual (no file modification)
- Uses InlayHintsProvider for clean `<AXX>` / `<UXXXX>` display in raw mode
- Multi-language support (Java, Kotlin, Python, JavaScript, etc.)

## Usage
1. Open any file with string literals containing `\x` or `\u`
2. Press `Ctrl+Shift+E` or click the toolbar button
3. Watch escapes turn into `<AXX>` / `<UXXXX>` or back to real characters

## Architecture
- Pure visual (no file modification)
- Uses Annotator + RangeHighlighter for performance
- Future: Full Inlay replacement for seamless editing

## Version
1.0.0 - Initial release (May 2026)

Developed by DC7C5 as part of Python + C/C++ 4-Agent Team orchestration.

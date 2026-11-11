# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a dual-language report generation tool that creates PDF reports from JSON data using JasperReports:

- **Java tool**: Command-line application using JasperReports Library (LGPL licensed)
- **Python tool**: Python binding that wraps the Java tool (MIT licensed)

## Build Commands

### Development Build
```bash
# Build both Java and Python components
pnpm run build

# Build Java component only
pnpm run build:java

# Build Python component only  
pnpm run build:python
```

### Release Build
```bash
# Create release package
pnpm run release
```

### Testing
```bash
# Run Java application with test data
pnpm run exec:java

# Test with sample data (ipaexg.ttf must be placed in etc/fonts/ beforehand)
REPCMD_FONT_DIR=$(pwd)/etc/fonts dist/repcmd.sh < ./etc/test.json > test.pdf
```

## Architecture

### Java Component (`java/`)
- **Main class**: `jp.infodb.repcmd.App` - Entry point that reads JSON from stdin and outputs PDF to stdout
- **Report class**: `jp.infodb.repcmd.Report` - Wraps JasperReports functionality for template processing
- **Dependencies**: JasperReports 6.21.5, Jackson for JSON parsing
- **Build**: Maven-based (compiler source/target in `pom.xml` is still 1.7, but dependencies require Java 8+)

### Python Component (`python/`)
- **Main module**: `repcmd/__init__.py` - Provides `execute(data, font_dir=None)` that calls Java tool via subprocess; `font_dir` (or env `REPCMD_FONT_DIR`) is passed to `repcmd.sh` as an absolute path, and `RepcmdError` is raised on non-zero exit or empty output
- **Build**: Poetry-based packaging
- **Python version**: Requires Python 3.9+

### Fonts
- Font files are NOT bundled (since v1.3.0) because the bundled IPAex font lacked its license file
- `repcmd.sh` appends `$REPCMD_FONT_DIR` to the classpath; that directory is a JasperReports font extension (`jasperreports_extension.properties` + font XML + font files)
- Templates for the extension files are in `etc/fonts/`; font files there are git-ignored and must never be committed

### Data Flow
1. JSON input contains `template` (JasperReports template path) and `data` (array of datasets)
2. Each dataset can have `properties` (key-value pairs) and `detail` (table data)
3. Java tool processes multiple datasets and concatenates pages into single PDF
4. Python tool acts as a wrapper around the Java command-line tool

### Key Files
- `java/pom.xml`: Maven configuration with JasperReports dependency
- `python/pyproject.toml`: Poetry configuration
- `java/release.sh`: Script to package Java distribution with dependencies
- `etc/test.json`: Sample input data for testing
- `etc/fonts/`: Font extension templates (font files are obtained separately)

## Version Update Procedure

When updating the version number, the following files need to be updated:

1. **Project Root**:
   - `package.json`: Update `version` field

2. **Java Component**:
   - `java/pom.xml`: Update `<version>` tag
   - `java/release.sh`: Update JAR filename in copy command
   - `java/sh/repcmd.sh`: Update JAR filename in CLASSPATH

3. **Python Component**:
   - `python/pyproject.toml`: Update `version` field
   - `python/repcmd/__init__.py`: Update `__version__` variable
   - `python/repcmd/setup.py`: Update `release_tag` variable

3. **Testing**: Run build after version update to ensure everything works:
   ```bash
   pnpm run build
   ```

## Development Notes

- Version is maintained separately in `pom.xml` (Java) and `pyproject.toml` (Python)
- Java component is built and run with JDK/JRE 17. Java 7 does NOT work: JasperReports 6.21.5, Jackson 2.17 and OpenPDF are compiled for Java 8 (verified: UnsupportedClassVersionError on OpenJDK 1.7.0_352)
- The `pom.xml` compiler target (1.7) is outdated and will be updated separately; Java 17 is not available on Debian 13, so a newer JDK must be verified
- Python component shells out to Java tool, requiring Java runtime in deployment
- Templates are JasperReports .jasper files (compiled .jrxml templates)
- `mvn exec:java` (`pnpm run exec:java`) has no fonts on its classpath, so the sample template fails unless a font extension is added
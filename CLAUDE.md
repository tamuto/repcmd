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

# Test with sample data
dist/repcmd.sh < ./etc/test.json > test.pdf
```

## Architecture

### Java Component (`java/`)
- **Main class**: `jp.infodb.repcmd.App` - Entry point that reads JSON from stdin and outputs PDF to stdout
- **Report class**: `jp.infodb.repcmd.Report` - Wraps JasperReports functionality for template processing
- **Dependencies**: JasperReports 6.20.0, Jackson for JSON parsing
- **Build**: Maven-based with Java 1.7 compatibility

### Python Component (`python/`)
- **Main module**: `repcmd/__init__.py` - Provides `execute()` function that calls Java tool via subprocess
- **Build**: Poetry-based packaging
- **Python version**: Requires Python 3.9+

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

## Version Update Procedure

When updating the version number, the following files need to be updated:

1. **Java Component**:
   - `java/pom.xml`: Update `<version>` tag
   - `java/release.sh`: Update JAR filename in copy command
   - `java/sh/repcmd.sh`: Update JAR filename in CLASSPATH

2. **Python Component**:
   - `python/pyproject.toml`: Update `version` field
   - `python/repcmd/__init__.py`: Update `__version__` variable
   - `python/repcmd/setup.py`: Update `release_tag` variable

3. **Testing**: Run build after version update to ensure everything works:
   ```bash
   pnpm run build
   ```

## Development Notes

- Version is maintained separately in `pom.xml` (Java) and `pyproject.toml` (Python)
- Java component requires JDK 17+ for building but targets Java 1.7 runtime
- Python component shells out to Java tool, requiring Java runtime in deployment
- Templates are JasperReports .jasper files (compiled .jrxml templates)
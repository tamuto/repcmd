# Report Command Tool

This project is an open-source software that includes both Java and Python tools.

## Java Command-line Tool

The Java tool is a command-line tool that uses the JasperReports Library and is distributed under the LGPL.
This tool includes the following functionality:

* Receives data in JSON format, embeds the data into a report template, and outputs a PDF.

### Usage

```sh
repcmd.sh < data.json > out.pdf
```

### Build

```
apt-get update
apt-get install openjdk-17-jdk maven

pnpm run build:java
pnpm run release
```

### Sample Report

```
dist/repcmd.sh < ./etc/test.json > test.pdf
```

* Python Binding Tool

<<under consideration>>

```python
import repcmd

result = repcmd.execute(data)
with open('out.pdf', 'wb') as f:
    f.write(result)
```

## License

This project is provided under the LGPL license for the Java tool and the MIT license for the Python tool.
Please refer to the respective license files for more details.

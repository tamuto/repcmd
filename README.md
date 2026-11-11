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

### Fonts

Font files are **not** included in this tool (since v1.3.0).
Prepare a font directory and specify it with the `REPCMD_FONT_DIR` environment variable.
The directory is added to the Java classpath as a JasperReports font extension.

```
fonts/                                   <- REPCMD_FONT_DIR
├── jasperreports_extension.properties
├── fonts.xml
├── ipaexg.ttf                           <- obtain it yourself
└── (license file of the font)
```

Templates for `jasperreports_extension.properties` and `fonts.xml` are in `etc/fonts/`.
The sample template uses the font family `ipaexg` (IPAex Gothic).
Download it from https://moji.or.jp/ipafont/ipafontdownload/ , place `ipaexg.ttf` in `etc/fonts/`
and keep the IPA Font License Agreement file from the download together with it.

To use other fonts, add the font files and add `<fontFamily>` entries to `fonts.xml`.

### Sample Report

```
REPCMD_FONT_DIR=$(pwd)/etc/fonts dist/repcmd.sh < ./etc/test.json > test.pdf
```

* Python Binding Tool

<<under consideration>>

```python
import repcmd

result = repcmd.execute(data)
with open('out.pdf', 'wb') as f:
    f.write(result)
```

The font directory can be passed as `repcmd.execute(data, font_dir='path/to/fonts')`.
If omitted, the `REPCMD_FONT_DIR` environment variable is used.
`repcmd.RepcmdError` is raised when the report generation fails.

## Notice about versions before v1.3.0

Versions before v1.3.0 bundled the IPAex font without the IPA Font License Agreement,
which the license requires on redistribution.
Because of this, those releases have been withdrawn, and the font is no longer distributed with this project.
If you have an old release, please obtain the font from the official site instead.

## License

This project is provided under the LGPL license for the Java tool and the MIT license for the Python tool.
Please refer to the respective license files for more details.

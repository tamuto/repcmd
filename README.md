# Report Command Tool

## Usage

* directory

```sh
java -jar repcmd.jar < data.json > out.pdf
```

* docker

```sh
docker run -it tamuto/repcmd < data.json > out.pdf
```

* Python Binding

<<under consideration>>

```python
import repcmd

result = repcmd.execute(data)
with open('out.pdf', 'wb') as f:
    f.write(result)
```

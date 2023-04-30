import urllib
import tarfile
import io
import os

from . import __version__


if __name__ == '__main__':
    url = f'https://{__version__}'

    response = urllib.request.urlopen(url)
    
    with tarfile.open(fileobj=io.BytesIO(response.read()), mode='r:gz') as tar:
        extract_dir = os.path.dirname(__file__)
        tar.extractall(path=extract_dir)

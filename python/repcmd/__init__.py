import subprocess
import json
import os

__version__ = '1.3.0'


class RepcmdError(RuntimeError):
    def __init__(self, message, returncode, stderr):
        super().__init__(message)
        self.returncode = returncode
        self.stderr = stderr


def execute(data, font_dir=None):
    """Generate a PDF from report data and return it as bytes.

    font_dir: directory containing jasperreports_extension.properties,
    the font family XML and the font files (see etc/fonts in the
    repository). If omitted, the REPCMD_FONT_DIR environment variable
    is used.
    """
    font_dir = font_dir or os.environ.get('REPCMD_FONT_DIR')
    env = dict(os.environ)
    if font_dir:
        # repcmd.sh changes its working directory, so pass an absolute path
        font_dir = os.path.abspath(font_dir)
        if not os.path.isdir(font_dir):
            raise FileNotFoundError(f'font directory not found: {font_dir}')
        env['REPCMD_FONT_DIR'] = font_dir

    inp = json.dumps(data, ensure_ascii=False)
    cmd = f'{os.path.dirname(__file__)}/repcmd.sh'
    result = subprocess.run(
        [cmd],
        input=inp.encode(),
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
        env=env)
    stderr = result.stderr.decode(errors='replace')
    if result.returncode != 0:
        raise RepcmdError(
            f'repcmd failed (exit code {result.returncode}):\n{stderr}',
            result.returncode, stderr)
    if not result.stdout:
        raise RepcmdError(
            f'repcmd produced no output:\n{stderr}',
            result.returncode, stderr)
    return result.stdout

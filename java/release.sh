set -eu
rm -rf dist
mkdir -p dist/lib
cp java/target/repcmd-1.2.0.jar dist/
cp java/target/dependency/*.jar dist/lib
cp java/sh/repcmd.sh dist/
cd dist
tar zcf ../repcmd.tar.gz *

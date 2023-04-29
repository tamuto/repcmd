#!/bin/bash
cd `dirname $0`
CLASSPATH=.:repcmd-1.0.jar
for i in lib/*.jar; do
  CLASSPATH=$CLASSPATH:$i
done
java -cp $CLASSPATH jp.infodb.repcmd.App < $1

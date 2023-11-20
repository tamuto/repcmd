#!/bin/bash
cd `dirname $0`
CLASSPATH=.:repcmd-1.1.0.jar
for i in lib/*.jar; do
  CLASSPATH=$CLASSPATH:$i
done

read input_data
echo $input_data | java -cp $CLASSPATH jp.infodb.repcmd.App

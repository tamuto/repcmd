#!/bin/bash
cd `dirname $0`
CLASSPATH=.:repcmd-1.3.0.jar
for i in lib/*.jar; do
  CLASSPATH=$CLASSPATH:$i
done
if [ -n "$REPCMD_FONT_DIR" ]; then
  CLASSPATH=$CLASSPATH:$REPCMD_FONT_DIR
fi

read input_data
echo $input_data | java -cp $CLASSPATH jp.infodb.repcmd.App

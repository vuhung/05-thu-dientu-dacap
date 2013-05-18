#!/bin/sh

#Set path variable
HOME_PROJECT="."
JS_PATH="$HOME_PROJECT/js/inet"
DEPLOY_PATH="$HOME_PROJECT/js/inet"
DEPLOY_FILE="inet-base.js"

# 
# Compress java script file
# @param file_name 	: the given file name
# Ex : js_build javascript.js
#
js_build()
{
    OUTPUT=$DEPLOY_PATH/$DEPLOY_FILE
    FILE_NAME=$1
    echo "====Begin compressor $FILE_NAME "

    java -jar /home/tools/yuicompressor-2.4.2/build/yuicompressor-2.4.2.jar --type js $JS_PATH/$FILE_NAME >> $OUTPUT
    #rm $JS_PATH/$FILE_NAME
    echo " " >> $OUTPUT

    echo "====End compressor $FILE_NAME "
}


#Start
echo "Start build ........................."

#Prepare
mkdir -p $DEPLOY_PATH
cat licenses > $DEPLOY_PATH/$DEPLOY_FILE

#BUILD
js_build ui/common/google/gears.js
js_build iNet.js
#js_build data/Languages.js
js_build data/DocumentFormat.js

#end build
echo "Output is $DEPLOY_PATH/$DEPLOY_FILE"
echo "Finish build ....................OK"

#!/bin/sh

#Set path variable
HOME_PROJECT="."
CSS_PATH="$HOME_PROJECT/css"
DEPLOY_PATH="$HOME_PROJECT/build"

# 
# Compress java script file
# @param file_name 	: the given file name
# Ex : js_build javascript.js
#
css_build()
{
	FILE_NAME=$1
    OUTPUT=$DEPLOY_PATH/$FILE_NAME
    
    echo "====Begin compressor $FILE_NAME "

    java -jar /home/tools/yuicompressor-2.4.2/build/yuicompressor-2.4.2.jar --type css $CSS_PATH/$FILE_NAME > $OUTPUT
    echo " " >> $OUTPUT

    echo "====End compressor $FILE_NAME "
}


#Start
echo "Start build ........................."

#Prepare
mkdir -p $DEPLOY_PATH

#BUILD
css_build calendar.css
css_build column-tree.css
css_build common.css
css_build login.css
css_build mail.css
css_build paperwork.css
css_build title-dialog.css
css_build upload-dialog.css

#end build
echo "Finish build ....................OK"

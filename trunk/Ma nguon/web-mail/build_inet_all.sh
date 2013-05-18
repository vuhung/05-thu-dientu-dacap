#!/bin/sh

#Set path variable
HOME_PROJECT="."
JS_PATH="$HOME_PROJECT/js/inet"
DEPLOY_PATH="$HOME_PROJECT/js/inet"
DEPLOY_FILE="inet-all.js"

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
js_build data/db/Table.js
js_build data/db/DBProxy.js
js_build data/db/Connection.js
js_build data/db/GearsConnection.js
js_build data/db/Transaction.js
js_build utils/ModuleMgr.js
js_build utils/Date.js
js_build utils/IDGenerator.js
js_build ui/common/control/FollowUpPanel.js
js_build ui/common/dialog/Dialog.js
js_build ui/common/dialog/TitleDialog.js
js_build ui/common/form/SearchField.js
js_build ui/common/form/HtmlEditorPlugins.js
js_build ui/common/grid/HttpProxyWrapper.js
js_build ui/common/grid/WebOSGrid.js
js_build ui/common/grid/WebOSColumn.js
js_build ui/common/grid/WebOSGridService.js
js_build ui/common/grid/WebOSEditorGrid.js
js_build ui/common/Header.js
js_build ui/common/menu/Menu.js
js_build ui/common/search/AdvancedSearch.js
js_build ui/common/store/WebOSSimpleStore.js
js_build ui/common/store/WebOSGroupingStore.js
js_build ui/common/store/WebOSDBStore.js
js_build ui/common/store/WebOSDBGroupingStore.js
js_build ui/common/tab/TabCloseMenu.js
js_build ui/common/tree/TreeColumnNodeUI.js
js_build ui/common/tree/ColumnTreePanel.js
js_build ui/common/tree/TreeLoaderWrapper.js
js_build ui/common/tree/TreeNodeExUI.js
js_build ui/mail/addressbook/AddressBookPanel.js
js_build ui/mail/addressbook/ContactDialog.js
js_build ui/mail/MenuContext.js
js_build ui/mail/MailViewPanel.js
js_build ui/mail/ConfigAccount.js
js_build ui/mail/DialogConfigAccount.js
js_build ui/mail/DialogCreateFolder.js
js_build ui/mail/DialogEnterPassword.js
js_build ui/mail/DialogMailAddFilter.js
js_build ui/mail/DialogMailFilter.js
js_build ui/mail/DialogMailFolder.js
js_build ui/mail/DialogRenameFolder.js
js_build ui/mail/DialogSelectContact.js
js_build ui/mail/DialogSignature.js
js_build ui/mail/FilterClauseUnitPanel.js
js_build ui/mail/BoxSelect.js
js_build ui/mail/MailCompose.js
js_build ui/mail/MailFollowUpPanel.js
js_build ui/mail/MailReader.js
js_build ui/mail/MailSearch.js
js_build ui/mail/MailSearchPanel.js
js_build ui/mail/MailService.js
js_build ui/mail/MailToolbar.js
js_build ui/mail/TreeMailbox.js
js_build ui/mail/TreeSelectContact.js

#mv $DEPLOY_PATH/$DEPLOY_FILE $JS_PATH/temp.js
#js_build temp.js

#end build
echo "Output is $DEPLOY_PATH/$DEPLOY_FILE"
echo "Finish build ....................OK"

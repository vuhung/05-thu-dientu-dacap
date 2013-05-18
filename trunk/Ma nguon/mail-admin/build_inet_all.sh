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
js_build utils/Date.js
js_build utils/IDGenerator.js
js_build ui/common/dialog/Dialog.js
js_build ui/common/dialog/TitleDialog.js
js_build ui/common/grid/HttpProxyWrapper.js
js_build ui/common/grid/WebOSGrid.js
js_build ui/common/grid/WebOSColumn.js
js_build ui/common/grid/WebOSGridService.js
js_build ui/common/grid/WebOSEditorGrid.js
js_build ui/mail/Header.js
js_build ui/common/menu/Menu.js
js_build ui/common/search/AdvancedSearch.js
js_build ui/common/store/WebOSSimpleStore.js
js_build ui/common/store/WebOSGroupingStore.js
js_build ui/common/store/WebOSDBStore.js
js_build ui/common/store/WebOSDBGroupingStore.js
js_build ui/common/tab/TabCloseMenu.js
js_build ui/common/tree/TreeColumnNodeUI.js
js_build ui/common/tree/TreeLoaderWrapper.js
js_build ui/common/tree/TreeNodeExUI.js
js_build ui/LotusService.js
js_build ui/account/AccountPanel.js
js_build ui/account/AccountToolbar.js
js_build ui/account/CreateAccount.js
js_build ui/account/DialogAddAccount.js
js_build ui/account/DialogCreateAccount.js
js_build ui/account/DialogResetPwd.js
js_build ui/account/DialogSearchAccount.js
js_build ui/mail/AliasMailToolbar.js
js_build ui/mail/CheckValidator.js
js_build ui/mail/CreateAlias.js
js_build ui/mail/CreateEmail.js
js_build ui/mail/DialogCreateDomain.js
js_build ui/mail/DialogDepartment.js
js_build ui/mail/DialogForwardAddress.js
js_build ui/mail/DialogSearchAccount.js
js_build ui/mail/DialogSearchAlias.js
js_build ui/mail/DialogSelectedAlias.js
js_build ui/mail/InputEmailDialog.js
js_build ui/mail/MailPanel.js
js_build ui/mail/MailSearch.js
js_build ui/mail/MailToolbar.js
js_build ui/mail/ReportPanel.js
js_build ui/mail/ImportPanel.js
js_build ui/mail/ToolbarSearchAccount.js
js_build ui/mail/TreeMailbox.js
js_build ui/org/UserPanel.js
js_build ui/org/CreateGroupPanel.js
js_build ui/org/CreateUserPanel.js
js_build ui/org/DeleteUserDialog.js
js_build ui/org/GroupPanel.js
js_build ui/org/MemberGridPanel.js
js_build ui/org/SearchUserDialog.js
js_build ui/org/SearchUserToolbar.js

#mv $DEPLOY_PATH/$DEPLOY_FILE $JS_PATH/temp.js
#js_build temp.js

#end build
echo "Output is $DEPLOY_PATH/$DEPLOY_FILE"
echo "Finish build ....................OK"

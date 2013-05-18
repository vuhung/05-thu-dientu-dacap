<?xml version="1.0" encoding="utf-8"?>
<project path="" name="iwebos" author="Dung Nguyen" version="1.0" copyright="/*****************************************************************&#xD;&#xA;   Copyright 2008 by iNet Solutions (info@truthinet.com.vn)&#xD;&#xA;&#xD;&#xA;   Licensed under the iNet Solutions Corp.,;&#xD;&#xA;   you may not use this file except in compliance with the License.&#xD;&#xA;   You may obtain a copy of the License at&#xD;&#xA;&#xD;&#xA;       http://www.truthinet.com/licenses&#xD;&#xA;&#xD;&#xA;   Unless required by applicable law or agreed to in writing, software&#xD;&#xA;   distributed under the License is distributed on an &quot;AS IS&quot; BASIS,&#xD;&#xA;   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.&#xD;&#xA;   See the License for the specific language governing permissions and&#xD;&#xA;   limitations under the License.&#xD;&#xA;*****************************************************************/" output="$project\build" source="true" source-dir="$output\source" minify="true" min-dir="$output\build" doc="False" doc-dir="$output\docs" master="true" master-file="$output\yui-ext.js" zip="true" zip-file="$output\yuo-ext.$version.zip">
  <directory name="" />
  <target name="Core" file="$output\inet-base.js" debug="False" shorthand="False" shorthand-list="YAHOO.util.Dom.setStyle&#xD;&#xA;YAHOO.util.Dom.getStyle&#xD;&#xA;YAHOO.util.Dom.getRegion&#xD;&#xA;YAHOO.util.Dom.getViewportHeight&#xD;&#xA;YAHOO.util.Dom.getViewportWidth&#xD;&#xA;YAHOO.util.Dom.get&#xD;&#xA;YAHOO.util.Dom.getXY&#xD;&#xA;YAHOO.util.Dom.setXY&#xD;&#xA;YAHOO.util.CustomEvent&#xD;&#xA;YAHOO.util.Event.addListener&#xD;&#xA;YAHOO.util.Event.getEvent&#xD;&#xA;YAHOO.util.Event.getTarget&#xD;&#xA;YAHOO.util.Event.preventDefault&#xD;&#xA;YAHOO.util.Event.stopEvent&#xD;&#xA;YAHOO.util.Event.stopPropagation&#xD;&#xA;YAHOO.util.Event.stopEvent&#xD;&#xA;YAHOO.util.Anim&#xD;&#xA;YAHOO.util.Motion&#xD;&#xA;YAHOO.util.Connect.asyncRequest&#xD;&#xA;YAHOO.util.Connect.setForm&#xD;&#xA;YAHOO.util.Dom&#xD;&#xA;YAHOO.util.Event">
    <include name="ui\common\google\gears.js" />
    <include name="iNet.js" />
    <include name="ui\paperwork\ed\Languages.js" />
    <include name="data\DocumentFormat.js" />
  </target>
  <target name="Everything" file="$output\inet-all.js" debug="False" shorthand="False" shorthand-list="YAHOO.util.Dom.setStyle&#xD;&#xA;YAHOO.util.Dom.getStyle&#xD;&#xA;YAHOO.util.Dom.getRegion&#xD;&#xA;YAHOO.util.Dom.getViewportHeight&#xD;&#xA;YAHOO.util.Dom.getViewportWidth&#xD;&#xA;YAHOO.util.Dom.get&#xD;&#xA;YAHOO.util.Dom.getXY&#xD;&#xA;YAHOO.util.Dom.setXY&#xD;&#xA;YAHOO.util.CustomEvent&#xD;&#xA;YAHOO.util.Event.addListener&#xD;&#xA;YAHOO.util.Event.getEvent&#xD;&#xA;YAHOO.util.Event.getTarget&#xD;&#xA;YAHOO.util.Event.preventDefault&#xD;&#xA;YAHOO.util.Event.stopEvent&#xD;&#xA;YAHOO.util.Event.stopPropagation&#xD;&#xA;YAHOO.util.Event.stopEvent&#xD;&#xA;YAHOO.util.Anim&#xD;&#xA;YAHOO.util.Motion&#xD;&#xA;YAHOO.util.Connect.asyncRequest&#xD;&#xA;YAHOO.util.Connect.setForm&#xD;&#xA;YAHOO.util.Dom&#xD;&#xA;YAHOO.util.Event">
    <include name="utils\IDGenerator.js" />
    <include name="utils\Date.js" />
    <include name="utils\ModuleMgr.js" />
    <include name="data\db\Connection.js" />
    <include name="data\db\DBProxy.js" />
    <include name="data\db\GearsConnection.js" />
    <include name="data\db\Table.js" />
    <include name="data\db\Transaction.js" />
    <include name="ui\common\control\FollowUpPanel.js" />
    <include name="ui\common\dialog\Dialog.js" />
    <include name="ui\common\dialog\TitleDialog.js" />
    <include name="ui\common\dialog\UserDialog.js" />
    <include name="ui\common\dialog\ComponentDialog.js" />
    <include name="ui\common\form\SearchField.js" />
    <include name="ui\common\grid\WebOSColumn.js" />
    <include name="ui\common\grid\WebOSEditorGrid.js" />
    <include name="ui\common\grid\WebOSGrid.js" />
    <include name="ui\common\grid\WebOSGridService.js" />
    <include name="ui\common\search\AdvancedSearch.js" />
    <include name="ui\common\store\WebOSDBGroupingStore.js" />
    <include name="ui\common\store\WebOSDBStore.js" />
    <include name="ui\common\store\WebOSGroupingStore.js" />
    <include name="ui\common\store\WebOSSimpleStore.js" />
    <include name="ui\common\tab\TabCloseMenu.js" />
    <include name="ui\common\tree\ColumnTreePanel.js" />
    <include name="ui\common\tree\TreeColumnNodeUI.js" />
    <include name="ui\common\tree\TreeNodeExUI.js" />
    <include name="ui\mail\store\MailStore.js" />
    <include name="ui\mail\DialogCreateFolder.js" />
    <include name="ui\mail\DialogRenameFolder.js" />
    <include name="ui\mail\MailCompose.js" />
    <include name="ui\mail\MailComposeToolbar.js" />
    <include name="ui\mail\MailPanel.js" />
    <include name="ui\mail\MailReader.js" />
    <include name="ui\mail\MailSearch.js" />
    <include name="ui\mail\MailToolbar.js" />
    <include name="ui\mail\TreeMailbox.js" />
    <include name="ui\paperwork\dw\DocumentWorkSearchDetails.js" />
    <include name="ui\paperwork\dw\DocumentWorkService.js" />
    <include name="ui\paperwork\dw\DraftDocumentService.js" />
    <include name="ui\paperwork\ed\EDInDWDialog.js" />
    <include name="ui\paperwork\dw\NewDocumentWorkPanel.js" />
    <include name="ui\paperwork\dw\NewDraftDocumentPanel.js" />
    <include name="ui\paperwork\dw\NewSendInfoPanel.js" />
    <include name="ui\paperwork\dw\PageCopyFromOriginal.js" />
    <include name="ui\paperwork\dw\PageExchange.js" />
    <include name="ui\paperwork\dw\PageSubmit.js" />
    <include name="ui\paperwork\dw\ProcessPaperWorkPanel.js" />
    <include name="ui\paperwork\ed\CheckValidator.js" />
    <include name="ui\paperwork\ed\CreateReportProfessionPanel.js" />
    <include name="ui\paperwork\ed\DocumentReportInPanel.js" />
    <include name="ui\paperwork\ed\DocumentReportOutPanel.js" />
    <include name="ui\paperwork\ed\DocumentSystemDialog.js" />
    <include name="ui\paperwork\ed\DocumentSystemDialogSearchDetails.js" />
    <include name="ui\paperwork\ed\DraftDocumentSearchDetails.js" />
    <include name="ui\paperwork\ed\EDDialog.js" />
    <include name="ui\paperwork\ed\EDSearchDetails.js" />
    <include name="ui\paperwork\ed\Header.js" />
    <include name="ui\paperwork\ed\NewDocumentInPanel.js" />
    <include name="ui\paperwork\ed\NewDocumentOutPanel.js" />
    <include name="ui\paperwork\ed\NotificationRecordPanel.js" />
    <include name="ui\paperwork\ed\PrecompletedDocumentPanel.js" />
    <include name="ui\paperwork\ed\ProcessDocumentInPanel.js" />
    <include name="ui\paperwork\ed\ProcessDocumentSubmitPanel.js" />
    <include name="ui\paperwork\ed\ProcessNotificationRecordPanel.js" />
    <include name="ui\paperwork\ed\ProcessRecordCompletePanel.js" />
    <include name="ui\paperwork\ed\ProcessTaskPanel.js" />
    <include name="ui\paperwork\ed\PublishDocPanel.js" />
    <include name="ui\paperwork\ed\ReadBusinessReport.js" />
    <include name="ui\paperwork\ed\ReadPublishDocument.js" />
    <include name="ui\paperwork\ed\ReceiveDocPanel.js" />
    <include name="ui\paperwork\ed\ReportDocumentDialog.js" />
    <include name="ui\paperwork\ed\ReportListDialog.js" />
    <include name="ui\paperwork\ed\UnitDialog.js" />
    <include name="ui\paperwork\ed\WatchDocumentSubmitPanel.js" />
    <include name="ui\paperwork\ed\WatchForDraftDocumentPanel.js" />
    <include name="ui\paperwork\ed\WatchForInDocumentPanel.js" />
    <include name="ui\paperwork\ed\WatchForNotificationPanel.js" />
    <include name="ui\paperwork\ed\WatchForOutDocumentPanel.js" />
    <include name="ui\paperwork\ed\WatchPaperWorkPanel.js" />
    <include name="ui\paperwork\store\PaperworkStore.js" />
    <include name="ui\paperwork\PaperworkFollowUpPanel.js" />
    <include name="ui\paperwork\PaperworkUIService.js" />
    <include name="ui\common\menu\Menu.js" />
  </target>
  <file name="data\db\Connection.js" path="data\db" />
  <file name="data\db\DBProxy.js" path="data\db" />
  <file name="data\db\GearsConnection.js" path="data\db" />
  <file name="data\db\Table.js" path="data\db" />
  <file name="data\db\Transaction.js" path="data\db" />
  <file name="data\DocumentFormat.js" path="data" />
  <file name="ui\common\control\FollowUpPanel.js" path="ui\common\control" />
  <file name="ui\common\dialog\ComponentDialog.js" path="ui\common\dialog" />
  <file name="ui\common\dialog\TitleDialog.js" path="ui\common\dialog" />
  <file name="ui\common\dialog\Dialog.js" path="ui\common\dialog" />
  <file name="ui\common\dialog\UserDialog.js" path="ui\common\dialog" />
  <file name="ui\common\form\SearchField.js" path="ui\common\form" />
  <file name="ui\common\grid\WebOSColumn.js" path="ui\common\grid" />
  <file name="ui\common\grid\WebOSEditorGrid.js" path="ui\common\grid" />
  <file name="ui\common\grid\WebOSGrid.js" path="ui\common\grid" />
  <file name="ui\common\grid\WebOSGridService.js" path="ui\common\grid" />  
  <file name="ui\common\google\gears.js" path="ui\common\google" />
  <file name="ui\common\menu\Menu.js" path="ui\common\menu" />
  <file name="ui\common\search\AdvancedSearch.js" path="ui\common\search" />
  <file name="ui\common\store\WebOSDBGroupingStore.js" path="ui\common\store" />
  <file name="ui\common\store\WebOSDBStore.js" path="ui\common\store" />
  <file name="ui\common\store\WebOSGroupingStore.js" path="ui\common\store" />
  <file name="ui\common\store\WebOSSimpleStore.js" path="ui\common\store" />
  <file name="ui\common\tab\TabCloseMenu.js" path="ui\common\tab" />
  <file name="ui\common\tree\ColumnTreePanel.js" path="ui\common\tree" />
  <file name="ui\common\tree\TreeColumnNodeUI.js" path="ui\common\tree" />
  <file name="ui\common\tree\TreeNodeExUI.js" path="ui\common\tree" />
  <file name="ui\mail\store\MailStore.js" path="ui\mail\store" />
  <file name="ui\mail\DialogCreateFolder.js" path="ui\mail" />
  <file name="ui\mail\DialogRenameFolder.js" path="ui\mail" />
  <file name="ui\mail\MailCompose.js" path="ui\mail" />
  <file name="ui\mail\MailComposeToolbar.js" path="ui\mail" />
  <file name="ui\mail\MailPanel.js" path="ui\mail" />
  <file name="ui\mail\MailReader.js" path="ui\mail" />
  <file name="ui\mail\MailSearch.js" path="ui\mail" />
  <file name="ui\mail\MailToolbar.js" path="ui\mail" />
  <file name="ui\mail\TreeMailbox.js" path="ui\mail" />
  <file name="ui\paperwork\dw\DocumentWorkSearchDetails.js" path="ui\paperwork\dw" />
  <file name="ui\paperwork\dw\DocumentWorkService.js" path="ui\paperwork\dw" />
  <file name="ui\paperwork\dw\DraftDocumentService.js" path="ui\paperwork\dw" />
  <file name="ui\paperwork\ed\EDInDWDialog.js" path="ui\paperwork\dw" />  
  <file name="ui\paperwork\dw\NewDocumentWorkPanel.js" path="ui\paperwork\dw" />
  <file name="ui\paperwork\dw\NewDraftDocumentPanel.js" path="ui\paperwork\dw" />
  <file name="ui\paperwork\dw\NewSendInfoPanel.js" path="ui\paperwork\dw" />
  <file name="ui\paperwork\dw\PageCopyFromOriginal.js" path="ui\paperwork\dw" />
  <file name="ui\paperwork\dw\PageExchange.js" path="ui\paperwork\dw" />
  <file name="ui\paperwork\dw\PageSubmit.js" path="ui\paperwork\dw" />
  <file name="ui\paperwork\dw\ProcessPaperWorkPanel.js" path="ui\paperwork\dw" />
  <file name="ui\paperwork\ed\CheckValidator.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\CreateReportProfessionPanel.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\DocumentReportInPanel.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\DocumentReportOutPanel.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\DocumentSystemDialog.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\DocumentSystemDialogSearchDetails.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\DraftDocumentSearchDetails.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\EDDialog.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\EDSearchDetails.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\Header.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\Languages.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\NewDocumentInPanel.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\NewDocumentOutPanel.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\NotificationRecordPanel.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\PrecompletedDocumentPanel.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\ProcessDocumentInPanel.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\ProcessDocumentSubmitPanel.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\ProcessNotificationRecordPanel.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\ProcessRecordCompletePanel.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\ProcessTaskPanel.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\PublishDocPanel.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\ReadBusinessReport.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\ReadPublishDocument.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\ReceiveDocPanel.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\ReportDocumentDialog.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\ReportListDialog.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\UnitDialog.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\WatchDocumentSubmitPanel.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\WatchForDraftDocumentPanel.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\WatchForInDocumentPanel.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\WatchForNotificationPanel.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\WatchForOutDocumentPanel.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\WatchPaperWorkPanel.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\store\PaperworkStore.js" path="ui\paperwork\store" />
  <file name="ui\paperwork\PaperworkFollowUpPanel.js" path="ui\paperwork" />
  <file name="ui\paperwork\PaperworkUIService.js" path="ui\paperwork" />
  <file name="utils\Date.js" path="utils" />
  <file name="utils\IDGenerator.js" path="utils" />
  <file name="utils\ModuleMgr.js" path="utils" />
  <file name="iNet.js" path="" />
  <file name="ui\paperwork\dw\ProcessPaperWork.js" path="ui\paperwork\dw" />
  <file name="ui\paperwork\ed\CreateReportProfession.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\DocumentReportIn.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\DocumentReportOut.js" path="ui\paperwork\ed" />
  <file name="ui\mail\MailView.js" path="ui\mail" />
  <file name="ui\paperwork\ed\NotificationRecord.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\ProcessDocumentIn.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\PrecompletedDocument.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\ProcessDocumentSubmit.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\ProcessNotificationRecord.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\ProcessRecordComplete.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\ProcessTask.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\PublishDoc.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\ReceiveDoc.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\WatchDocumentSubmit.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\WatchForDraftDocument.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\WatchForInDocument.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\WatchForNotification.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\WatchForOutDocument.js" path="ui\paperwork\ed" />
  <file name="ui\paperwork\ed\WatchPaperWork.js" path="ui\paperwork\ed" />
  <file name="ui\summary\HomePage.js" path="ui\summary" />
</project>
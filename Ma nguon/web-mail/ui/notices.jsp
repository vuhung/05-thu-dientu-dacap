<%@include file="/ui/common/header.jsp"%>
<html xmlns="http://www.w3c.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><bean:message key="iwebos.title"/></title>
		<style type="text/css">
			.notice-body{width:100%;height:100%;background:#c3daf9;position:absolute;z-index:20000;left:0px;top:0px;}
			.notice-table{position:absolute;left:28%;top:40%;border:1px solid #6593cf;padding:2px;background:#c3daf9;z-index:20001;height:auto;width:500px;}
			.notice-table a{color:#225588;text-decoration:normal;}
			.notice-table .notice-indicator{border:1px solid #a3bad9;background:white;background-position:15px center;color:#444;font:bold 13px tahoma,arial,helvetica;padding:10px;margin:0;height:auto;}
			.notice-indicator .notice-immsg{font: bold 14px arial,tahoma,sans-serif;color: #ff0000;}
			.notice-indicator .notice-msg{font: bold 12px arial,tahoma,sans-serif;color: #0000ee;}
		</style>					
		<link href="images/favicon.ico" rel="shortcut icon"/>		
	</head>
	<body style="overflow:hidden">
		<tiles:insert name="body" flush="true"/>
	</body>	
</html>
<%@include file="/ui/common/header.jsp"%>
<html xmlns="http://www.w3c.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><bean:message key="iwebos.title"/></title>
		<style type="text/css">
			#loading-mask{width:100%;height:100%;background:#c3daf9;position:absolute;z-index:20000;left:0px;top:0px;}			
		    #loading{position:absolute;left:42%;top:40%;border:1px solid #6593cf;padding:2px;background:#c3daf9;z-index:20001;height:auto;}
		    #loading a {color:#225588;}
		    #loading .loading-indicator{border:1px solid #a3bad9;background:white;background-position: 15px center;color:#444;font:bold 13px tahoma,arial,helvetica;padding:10px;margin:0;height:auto;}
		    #loading-msg {font: normal 10px arial,tahoma,sans-serif;}					
		</style>
		<link href="images/favicon.ico" rel="shortcut icon"/>		
		<link rel="stylesheet" type="text/css" href="js/ext/resources/css/ext-all.css">
		<link rel="stylesheet" type="text/css" href="js/ext/resources/css/extjs.css"/>
		<link rel="stylesheet" type="text/css" href="css/common.css">
		<link rel="stylesheet" type="text/css" href="css/title-dialog.css">
		<link rel="stylesheet" type="text/css" href="css/paperwork.css">
		<link rel="stylesheet" type="text/css" href="css/mail.css">
		<link rel="stylesheet" type="text/css" href="css/upload-dialog.css">				
	</head>
	<body>
		<div id="loading-mask" style=""></div>
		<div id="loading">
		    <div class="loading-indicator"><img src="images/ics16/iwebos.gif" width="36" height="36" style="margin-right:8px;float:left;vertical-align:top;"/>iLotus - <a href="http://www.truthinet.com.vn/lotus/welcome.iws">iLotus Server 2009</a><br/><span id="loading-msg">Loading styles and images...</span></div>
		</div>	
		<script type="text/javascript">document.getElementById('loading-msg').innerHTML = 'Loading Core API...';</script>
		<script type="text/javascript" src="js/ext/adapter/ext/ext-base.js"></script>
		<script type="text/javascript">document.getElementById('loading-msg').innerHTML = 'Loading UI Components...';</script>
		<script type="text/javascript" src="js/ext/ext-all.js"></script>
		<script type="text/javascript">document.getElementById('loading-msg').innerHTML = 'Loading YUI components...';</script>
		<script type="text/javascript" src="js/ext/adapter/yui/yui-utilities.js"></script>
		<script type="text/javascript">document.getElementById('loading-msg').innerHTML = 'Loading iNet Core API...';</script>
		<script type="text/javascript" src="js/UploadDialog.js"></script>		
		<script type="text/javascript" src="js/inet/inet-base.js"></script>
		<script type="text/javascript">document.getElementById('loading-msg').innerHTML = 'Loading iNet components...';</script>
		<script type="text/javascript" src="js/inet/inet-all.js"></script>
				
		<tiles:insert name="header" flush="true"/>
		<tiles:insert name="menu" flush="true"/>
		<tiles:insert name="navigate" flush="true"/>
		<tiles:insert name="eheader" flush="true"/>
		<tiles:insert name="body" flush="true"/>
		
		<!-- view and download form -->
		<form id="viewdownload_">
			<input type="hidden" name="id"/>
			<input type="hidden" name="type"/>
			<input type="hidden" name="style"/>
			<input type="hidden" name="bean"/>
		</form>
		<script type="text/javascript">
			var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
			document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
		</script>
		<script type="text/javascript">
			try {
				var pageTracker = _gat._getTracker("UA-8726307-2");
				pageTracker._trackPageview();
			} catch(err) {}
		</script>
	</body>
	<script type="text/javascript">
		Ext.onReady(function(){
			setTimeout(function(){
			        Ext.get('loading').remove();
			        Ext.get('loading-mask').fadeOut({remove:true});
			    }, 250);	
		});
	</script>
</html>

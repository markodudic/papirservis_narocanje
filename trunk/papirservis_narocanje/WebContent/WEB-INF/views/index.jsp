<%@ page contentType="text/html;charset=UTF-8" language="java" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page session="true" buffer="16kb" import="java.util.*" isErrorPage="true"%>
<% session.setMaxInactiveInterval(8*60*60); %>


<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title id="lf_title">Naročanje odvozov</title>
	<link rel="stylesheet" href="jslib/jxlib/assets/themes/delicious/jxtheme.css" type="text/css" />
	<link rel="stylesheet" href="css/reset-fonts-grids.css" type="text/css" />
	<link rel="stylesheet" href="css/master.css" type="text/css" />
	<script type="text/javascript" src="jslib/mootools-1.2.5-core-nc.js"></script>
	<script type="text/javascript" src="jslib/mootools-1.2.4.4-more-yc.js"></script>
	<script type="text/javascript" src="js/locale.js"></script>
	<script type="text/javascript" src="jslib/jxlib/jxlib.js"></script>
	<script type="text/javascript" src="js/index.js"></script>
	<script type="text/javascript" src="js/login/crypt.js"></script>

	
</head>
<body>
	<jsp:include page="_header.jsp"/>
	<h5 id="h5"></h5>
	<div id='toolbar'>
	</div>
	<form id="loginForm" action="" method="get" target="">
		<div id="table-wrapper">
			<table id="logintable">
				<tr id="tr_login">
					<td id="td_login"><label id="l_username"></label></td>
					<td id="td_login"><input type="text" id="username" value="" size="30"/></td>
				</tr>
				<tr id="tr_login">
					<td id="td_login"><label id="l_password"></label></td>
					<td id="td_login"><input type="password" id="password" value="" size="30"/></td>
				</tr>
				<tr id="tr_login">
					<td id="td_error" colspan="2"><label id="l_error"></label></td></td>
				</tr>
			</table>
			

		</div>
	</form>
	<jsp:include page="_footer.jsp"/>
</body>
</html>

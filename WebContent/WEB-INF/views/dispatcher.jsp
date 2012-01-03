<%@ page contentType="text/html;charset=UTF-8" language="java" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page session="true" buffer="16kb" import="java.util.*" isErrorPage="true"%>

<% 
String login = session.getAttribute("status")+"";
if (login == null || !login.equals("0")) {
	response.sendRedirect("index");
	response.flushBuffer(); 
	return; 
}%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title id="lf_title">Naročanje odvozov</title>
	<link rel="stylesheet" href="jslib/jxlib/assets/themes/delicious/jxtheme.css" type="text/css" />
	<link rel="stylesheet" href="css/reset-fonts-grids.css" type="text/css" />
	<link rel="stylesheet" href="css/master.css" type="text/css" />
	<link rel="stylesheet" href="css/slimpicker.css" type="text/css" />
	<link rel="stylesheet" href="css/HtmlTable.Paginate.css" type="text/css" />
	<script type="text/javascript" src="jslib/mootools-1.2.5-core-nc.js"></script>
	<script type="text/javascript" src="jslib/mootools-1.2.4.4-more-yc.js"></script>
	<script type="text/javascript" src="jslib/HtmlTable.Paginate.js"></script>
	<script type="text/javascript" src="js/locale.js"></script>
	<script type="text/javascript" src="jslib/jxlib/jxlib.js"></script>
	<script type="text/javascript" src="js/dispatcher.js"></script>
	<script type="text/javascript" src="jslib/slimpicker.js"></script>
	<link rel="stylesheet" media="screen" href="css/omnigrid.css" type="text/css" />
	<script type="text/javascript" src="jslib/omnigrid.js"></script>
	
	
</head>
<body>
	<jsp:include page="_header.jsp"/>
	<h5 id="vnos"></h5>
	<div id='toolbar'>
	</div>




	<form id="listForms" action="" method="post" target="">
		<input type="hidden" id="ReportName" name="ReportName" value=""/>
		<input id='form_html' name='form_html' type="hidden" />
		<input id='form_title' name='form_title' type="hidden" />
		
		<div id="table-wrapper">
			<table id="searchtable">
				<tr id="tr_search">
					<td id="td_search"><label id="l_stranka"></label></td><td id="td_search"><select id="selectStranka" onchange="changeSubject()" ></select></td>
					<td id="td_search"><label id="l_datum"></label></td><td id="td_search"><input type="text" id="i_datum" value="" class="chooser-date"  /></td>
				</tr>
				<tr id="tr_search">
					<td id="td_search"><label id="l_naslov"></label></td><td id="td_search"><input type="text" id="i_naslov" value="" readonly/></td>
					<td id="td_search"><label id="l_material"></label></td><td id="td_search"><select id="selectMaterial" ></select></td>
				</tr>
				<tr id="tr_search">
					<td id="td_search"><label id="l_kraj"></label></td><td id="td_search"><input type="text" id="i_kraj" value="" readonly /></td>
					<td id="td_search"><label id="l_kolicina"></label></td><td id="td_search"><input type="text" id="i_kolicina" value="" /></td>
				</tr>
				<tr id="tr_search">
					<td id="td_search"><label id="l_kontakt"></label></td><td id="td_search"><input type="text" id="i_kontakt" value="" readonly /></td>
					<td id="td_search"><label id="l_osnovno"></label></td><td id="td_search"><input type="text" id="i_osnovno" value="" readonly /></select></td>
				</tr>
				<tr id="tr_search">
					<td id="td_search"><label id="l_telefon"></label></td><td id="td_search"><input type="text" id="i_telefon" value="" readonly /></td>
					<td id="td_search"><label id="l_opomba"></label></td><td id="td_search"><input type="text" id="i_opomba" value="" /></td>
				</tr>
				<tr id="tr_search">
					<td id="td_search"><label id="l_kupec"></label></td><td id="td_search"><input type="text" id="i_kupec" value="" readonly /></td>
					<td id="td_search"><label id="l_narocil"></label></td><td id="td_search"><select id="selectNarocil" ></select></td>
				</tr>
			</table>

		</div>
		
		<h5 id="odprta"></h5>	
		
		<div id='myTableGrid'></div>	
	</form>
	<jsp:include page="_footer.jsp"/>
</body>
</html>

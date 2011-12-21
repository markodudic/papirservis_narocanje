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
	<script type="text/javascript" src="js/inflate.js"></script>
	<link rel="stylesheet" media="screen" href="css/omnigrid.css" type="text/css" />
	<script type="text/javascript" src="jslib/omnigrid.js"></script>
	
</head>
<body>
	<jsp:include page="_header.jsp"/>
	<h5 id="h5"></h5>
	<div id='toolbar'>
		<!-- button type="button"></button-->
	</div>
	<form id="listForms" action="" method="post" target="">
		<!-- input type="hidden" id="confirmData" name="confirmData" value=""/ -->
		<!-- input type="hidden" id="refreshData" name="refreshData" value=""/-->
		<input type="hidden" id="ReportName" name="ReportName" value=""/>
		<input id='form_html' name='form_html' type="hidden" />
		<input id='form_title' name='form_title' type="hidden" />
		<div id="table-wrapper">
			<table id="searchtable">
				<tr id="tr_search">
					<td id="td_search"><label id="l_stranka"></label></td><td id="td_search"><select id="selectStranka" onchange="changeSubject()"></select></td>
					<td id="td_search"><label id="l_datum"></label></td><td id="td_search"><input type="text" id="datum" value="" class="chooser-date"  /></td>
				</tr>
				<tr id="tr_search">
					<td id="td_search"><label id="l_naslov"></label></td><td id="td_search"><input type="text" id="i_text" value="" /></td>
					<td id="td_search"><label id="l_odpadek"></label></td><td id="td_search"><select id="selectOdpadek" ></select></td>
				</tr>
				<tr id="tr_search">
					<td id="td_search"><label id="l_kraj"></label></td><td id="td_search"><input type="text" id="i_text" value="" /></td>
					<td id="td_search"><label id="l_kolicina"></label></td><td id="td_search"><input type="text" id="kolicina" value="" /></td>
				</tr>
				<tr id="tr_search">
					<td id="td_search"><label id="l_kontakt"></label></td><td id="td_search"><input type="text" id="i_text" value="" /></td>
					<td id="td_search"><label id="l_osnovno"></label></td><td id="td_search"><select id="selectOsnovno" ></select></td>
				</tr>
				<tr id="tr_search">
					<td id="td_search"><label id="l_telefon"></label></td><td id="td_search"><input type="text" id="i_text" value="" /></td>
					<td id="td_search"><label id="l_opomba"></label></td><td id="td_search"><input type="text" id="opomba" value="" /></td>
				</tr>
				<tr id="tr_search">
					<td id="td_search"><label id="l_kupec"></label></td><td id="td_search"><input type="text" id="i_text" value="" /></td>
					<td id="td_search"><label id="l_narocil"></label></td><td id="td_search"><select id="selectNarocil" ></select></td>
				</tr>
			</table>
			
			<!-- table id="listtable">
				<thead>
				<tr>
					<th id="lf_osvezi"></th>
					<th id="lf_dobavitelj"></th>
					<th id="lf_naslov"></th>
					<th id="lf_narocilo"></th>
					<th id="lf_evl"></th>
					<th id="lf_datum_nar"></th>
					<th id="lf_datum_izv"></th>
					<th id="lf_vrsta_odpadka"></th>
					<th id="lf_klas_stevilka"></th>
					<th id="lf_kolicina"></th>
					<th id="lf_placnik"></th>
					<th id="lf_kontakt"></th>
					<th id="lf_opomba"></th>
					<!-- th id="lf_status"></th-->
					<th id="lf_id_vozilo"></th>
				
				</tr>	
				</thead>	
				<tbody id="listtablebody">
				</tbody>
			</table-->
		</div>
		<div id='myTableGrid'></div>	
	</form>
	<jsp:include page="_footer.jsp"/>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Error</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/jslib/jxlib/assets/themes/delicious/jxtheme.css" type="text/css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset-fonts-grids.css" type="text/css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/master.css" type="text/css" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/mootools-1.2.5-core-nc.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/mootools-1.2.4.4-more-yc.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/locale.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/jxlib/jxlib.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/error.js"></script>
</head>
	
	
<body>
	<jsp:include page="_header.jsp"/>
	<h5 id='head'></h5>
		<div class="error_container">
			<h1 id='error_msg'></h1>
		</div>
	<jsp:include page="_footer.jsp"/>	
</body>

</html>
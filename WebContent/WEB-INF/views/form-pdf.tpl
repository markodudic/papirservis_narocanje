<?xml version="1.0" encoding="utf-8"?>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/xhtml+xml; charset=utf-8" />
<title>Form PDF</title>
<style>
* {
font-family: Arial;
font-size: small;
}
</style>
<link rel="stylesheet" href="${contextUrl}/css/reset-fonts-grids.css" type="text/css" />
<link rel="stylesheet" href="${contextUrl}/css/master.css" type="text/css" />
<link rel="stylesheet" href="${contextUrl}/css/form-pdf.css" type="text/css" />
</head>

<body>
<div id="hd">
<div id="logo">
	<a href="#" title="EKOL">
		<img src="${contextUrl}/img/ekol.gif" width="547" height="69" alt="logo"/>
	</a>
</div>
<div id="form_container">
<h5>${formTitle}</h5>
${formHtml}
</div>
</div>
</body>
</html>
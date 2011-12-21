<%@ page contentType="text/html;charset=UTF-8" language="java" %> 

<input type="hidden" id="status" name="status" value="<%out.println(session.getAttribute("status")+"");%>"/>
<input type="hidden" id="name" name="name" value="<%out.println(session.getAttribute("name")+"");%>"/>
<input type="hidden" id="surname" name="surname" value="<%out.println(session.getAttribute("surname")+"");%>"/>
		
<script type="text/javascript" src="${pageContext.request.contextPath}/js/_header.js"></script>

			<div id="wrapper">
			<div id="doc2">
				<div id="hd">
					<!-- div id="languageBar"></div -->

					<ul id="topnav">
						<li id="headerac"></li>
						<li id='headerlodiv'><a href="index" id="headerlo" title="" onclick="logout();"></a></li>
					</ul>
					<div id="logo">
						<a href="#" id="headerti" title="">
							<img id="img_logo" src="" width="221" height="61" alt="logo"/>
						</a>
					</div>
				</div>

				<div id="bdy">
					<div id="main">
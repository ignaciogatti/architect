<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><tiles:insertAttribute name="title" ignore="true" /></title>
<link rel="stylesheet" type="text/css" href="resources/css/theme1.css" />
<link rel="stylesheet" type="text/css" href="resources/css/style.css" />
<link rel="stylesheet" type="text/css" href="resources/css/nicetitle.css" />
<link rel="stylesheet" type="text/css" href="resources/css/jquery.gritter.css" />
<link rel="stylesheet" type="text/css" href="resources/css/jquery/redmond/jquery-ui-1.8.7.custom.css"/>
<link rel="stylesheet" type="text/css" href="resources/css/multiselect.css" />
<link rel="stylesheet" type="text/css" href="resources/js/plugins/dropdown/style.css" />
<script type="text/javascript" src="resources/js/jquery/jquery-1.8.3.js"></script>
<script type="text/javascript" src="resources/js/jquery/jquery-ui-1.9.2.custom.js"></script>
<script type="text/javascript" src="resources/js/plugins/nicetitle.js"></script>
<script type="text/javascript" src="resources/js/plugins/jquery.gritter.js"></script>
<script type="text/javascript" src="resources/js/plugins/jquery.tablesorter.js"></script>
<script type="text/javascript" src="resources/js/plugins/dropdown/dropdown.js"></script>
<script type="text/javascript" src="resources/js/plugins/ui.multiselect.js"></script>
<script type="text/javascript" src="resources/js/common/common.js"></script>
<!--[if IE]>
<link rel="stylesheet" type="text/css" href="resources/css/ie-sucks.css" />
<![endif]-->
</head>
<body>
	<div id="container">
		<div id="header">
        	<h2 id="header-title" >Architect</h2>
        	<h3 id="logged-in-txt" >Logged in as : <c:out value="${sessionScope.userName}"/></h3>
			<div id="topmenu">
                <ul>
                    <li class='<tiles:insertAttribute name="module" ignore="true" />'><a href="listModules">Modules</a></li>
                    <li class='<tiles:insertAttribute name="responsibility" ignore="true" />' > <a href="listResponsibilities">Responsibilities</a></li>
                    <li class='<tiles:insertAttribute name="dependency" ignore="true" />' > <a href="listDependencies">Dependencies</a></li>
                    <li class='<tiles:insertAttribute name="scenario" ignore="true" />' > <a href="listScenarios">Scenarios</a></li>
                    <li class='<tiles:insertAttribute name="designBots" ignore="true" />' ><a href="listDesignBots">Design Bots</a></li>
                    <li class='<tiles:insertAttribute name="diagram" ignore="true" />'><a href="diagram">Diagram</a></li>
                    <li class='<tiles:insertAttribute name="panel" ignore="true" />' ><a href="analysisPanel">Analysis Panel</a></li>
                </ul>
           	</div>
        </div>
	<div id="wrapper">
    	<tiles:insertAttribute name="body" />
        <tiles:insertAttribute  name="sidebar" />
    </div>
</div>
</body>
</html>
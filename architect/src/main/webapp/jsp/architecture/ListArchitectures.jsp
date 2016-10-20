<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><tiles:insertAttribute name="title" ignore="true" /></title>
<link rel="stylesheet" type="text/css" href="resources/css/theme1.css" />
<link rel="stylesheet" type="text/css" href="resources/css/style.css" />
<link rel="stylesheet" type="text/css" href="resources/css/nicetitle.css" />
<link rel="stylesheet" type="text/css" href="resources/css/jquery.gritter.css" />
<link rel="stylesheet" type="text/css" href="resources/css/jquery/redmond/jquery-ui-1.8.7.custom.css">
<link rel="stylesheet" type="text/css" href="resources/css/multiselect.css" />
<link rel="stylesheet" type="text/css" href="resources/js/plugins/dropdown/style.css" />
<script type="text/javascript" src="resources/js/jquery/jquery-1.8.3.js"></script>
<script type="text/javascript" src="resources/js/jquery/jquery-ui-1.9.2.custom.js"></script>
<script type="text/javascript" src="resources/js/plugins/nicetitle.js"></script>
<script type="text/javascript" src="resources/js/plugins/jquery.gritter.js"></script>
<script type="text/javascript" src="resources/js/plugins/jquery.tablesorter.js"></script>
<script type="text/javascript" src="resources/js/plugins/dropdown/dropdown.js"></script>
<script type="text/javascript" src="resources/js/plugins/ui.multiselect.js"></script>
<script type="text/javascript" src="resources/js/amq/amq_jquery_adapter.js"></script>
<script type="text/javascript" src="resources/js/amq/amq.js"></script>
<script type="text/javascript" src="resources/js/amq/syncUtils.js"></script>
<script type="text/javascript" src="resources/js/architecture/architecture.js"></script>
<script type="text/javascript" src="resources/js/common/common.js"></script>
<script type="text/javascript">
	USER = <c:out value="${sessionScope.user}"/>;
	LAST_CHANGE_NUMBER = <c:out value="${lastChangeNumber}"/>;
</script>
<!--[if IE]>
<link rel="stylesheet" type="text/css" href="resources/css/ie-sucks.css" />
<![endif]-->
</head>
<body>
	<div id="container">
		<input type="hidden" id="idDelete" />
		<div id="dialogEdit"></div>
		<div id="dialogAdd"></div>
		<div id="dialogDelete"><p style="font-size : 11px;">Are you sure you want to delete this architecture?</p></div>
		<div id="header">
			<h2 id="header-title" >Architect</h2>
        	<h3 id="logged-in-txt" >Logged in as : <c:out value="${sessionScope.userName}"/></h3>
			<div id="topmenu"></div>
        </div>
		<div id="wrapper">
            <div id="content" style="width : 100%; margin-top : 40px">
                <div id="box">
                	<h3>Architectures</h3>
                		<div class="btnAdd">
                           <a id="newArchitectureButton" href="#"><img src="resources/images/icons/add.png" />New Architecture</a>
                        </div>
						<table width="100%" class="tableScenarios">
                            <thead>
                                <tr>
                                    <th width="40px"><a href="#">Id<img src="resources/images/icons/arrow_down_mini.gif" width="16" height="16" /></a></th>
                                    <th><a href="#">Name</a></th>
                                    <th width="60px"></th>
                                </tr>
                            </thead>
                            <tbody id="tableBody">
                            	<c:forEach items="${architectures}" var="p">
                            		<jsp:include page="ArchitectureTableItem.jsp" flush="true">
										<jsp:param name="id" value="${p.id}"/>
										<jsp:param name="name" value="${p.name}"/>
									</jsp:include>
                           		</c:forEach>
                            </tbody>
						</table>
                	</div>
                <br />
            </div>
        </div>
     </div>
</body>
</html>
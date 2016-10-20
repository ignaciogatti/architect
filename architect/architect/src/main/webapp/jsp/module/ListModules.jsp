<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="resources/js/amq/amq_jquery_adapter.js"></script>
<script type="text/javascript" src="resources/js/amq/amq.js"></script>
<script type="text/javascript" src="resources/js/amq/syncUtils.js"></script>
<script type="text/javascript" src="resources/js/module/module.js"></script>
<script type="text/javascript">
	USER = <c:out value="${sessionScope.user}"/>;
	GROUP = <c:out value="${sessionScope.group}"/>;
	ARCHITECTURE = <c:out value="${sessionScope.architecture}"/>;
	LAST_CHANGE_NUMBER = <c:out value="${lastChangeNumber}"/>;
</script>
<input type="hidden" id="idDelete" />
<div id="dialogNew"></div>
<div id="dialogEdit"></div>
<div id="dialogDetail"></div>
<div id="dialogDelete"><p style="font-size : 11px;">Are you sure you want to delete this module?  </p></div>
<div id="top-panel">
    <div id="panel">
        <ul>
            <li><a id="newModule" href="#"><img src="resources/images/icons/add.png" /> New Module</a></li>
         </ul>
     </div>
</div>
    <div id="content">
        <div id="box">
            <h3>Modules</h3>
            <table class="tableScenarios">
                <thead>
                    <tr >
                        <th width="40px"><a href="#">ID<img src="resources/images/icons/arrow_down_mini.gif" width="16" height="16" /></a></th>
                        <th><a href="#">Name</a></th>
                        <th width="120px"></th>
                    </tr>
                </thead>
                <tbody id="tableBody">
                	<c:forEach items="${modules}" var="m">
                		<jsp:include page="ModuleTableItem.jsp" flush="true">
							<jsp:param name="id" value="${m.id}"/>
							<jsp:param name="name" value="${m.name}"/>
						</jsp:include>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
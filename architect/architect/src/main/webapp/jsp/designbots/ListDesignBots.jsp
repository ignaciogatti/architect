<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="resources/js/amq/amq_jquery_adapter.js"></script>
<script type="text/javascript" src="resources/js/amq/amq.js"></script>
<script type="text/javascript" src="resources/js/amq/syncUtils.js"></script>
<script type="text/javascript" src="resources/js/designbot/designbot.js"></script>
<script type="text/javascript">
	USER = <c:out value="${sessionScope.user}"/>;
	GROUP = <c:out value="${sessionScope.group}"/>;
	ARCHITECTURE = <c:out value="${sessionScope.architecture}"/>;
	LAST_CHANGE_NUMBER = <c:out value="${lastChangeNumber}"/>;
</script>
<input type="hidden" id="idDelete" />
<div id="dialogDelete"><p style="font-size : 11px;">Are you sure you want to delete this Design Bot?  </p></div>
<div id="dialogNew"></div>
<div id="dialogEdit"></div>
<div id="dialogDetail"></div>
<div id="top-panel">
    <div id="panel">
        <ul>
            <li><a id="newDesignBot" href="#"><img src="resources/images/icons/add.png" /> New Design Bot</a></li>
         </ul>
     </div>
</div>
<div id="content">
    <div id="box">
        <h3>Design Bots</h3>
        <table class="tableScenarios">
            <thead>
                <tr>
                    <th width="40px"><a href="#">ID<img src="resources/images/icons/arrow_down_mini.gif" width="16" height="16" /></a></th>
                    <th class="header"><a href="#">Name</a></th>
                    <th class="header"><a href="#">Quality Attribute</a></th>
                    <th width="90px" ></th>
                </tr>
            </thead>
            <tbody id="tableBody">
            	<c:forEach items="${designbots}" var="d">
                    <jsp:include page="DesignBotTableItem.jsp" flush="true">
							<jsp:param name="id" value="${d.id}"/>
							<jsp:param name="name" value="${d.name}"/>
							<jsp:param name="qualityAttribute" value="${d.qualityAttribute.name}"/>
						</jsp:include>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>
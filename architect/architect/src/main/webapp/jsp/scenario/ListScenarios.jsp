<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="resources/js/amq/amq_jquery_adapter.js"></script>
<script type="text/javascript" src="resources/js/amq/amq.js"></script>
<script type="text/javascript" src="resources/js/amq/syncUtils.js"></script>
<script type="text/javascript" src="resources/js/scenario/scenario.js"></script>
<script type="text/javascript">
	USER = <c:out value="${sessionScope.user}"/>;
	GROUP = <c:out value="${sessionScope.group}"/>;
	ARCHITECTURE = <c:out value="${sessionScope.architecture}"/>;
	LAST_CHANGE_NUMBER = <c:out value="${lastChangeNumber}"/>;
</script>
<div id="dialogNew"></div>
<div id="dialogResp"></div>
<div id="dialogEdit"></div>
<div id="dialogDetail"></div>
<jsp:include page="DeleteScenario.jsp" flush="true"/>
<div id="top-panel">
    <div id="panel">
        <ul>
			<li>
				<a id="newScenarioButton" href="#">
					<img src="resources/images/icons/add.png" /> New Scenario
				</a>
			</li>
		</ul>
     </div>
</div>
    <div id="content">
        <div id="box">
            <h3>Scenarios</h3>
            <table class="tableScenarios">
                <thead>
                    <tr >
                        <th width="40px"><a href="#">ID<img src="resources/images/icons/arrow_down_mini.gif" width="16" height="16" /></a></th>
                        <th width="90px"><a href="#">Name</a></th>
                        <th width="90px"><a href="#">Type</a></th>
                        <th width="90px"><a href="#">Priority</a></th>
                        <th width="90px"><a href="#">Response</a></th>
                        <th width="75px"></th>
                    </tr>
                </thead>
                <tbody id="tableBody">
                	<c:forEach items="${scenarios}" var="s">
                		<jsp:include page="ScenarioTableItem.jsp" flush="true">
							<jsp:param name="id" value="${s.id}"/>
							<jsp:param name="name" value="${s.name}"/>
							<jsp:param name="type" value="${s.qualityAttribute.name}"/>
							<jsp:param name="priority" value="${s.priority}"/>
							<jsp:param name="response" value="${s.getResponse().getLabel()}"/>
							<jsp:param name="measure" value="${s.measure}"/>
						</jsp:include>
                    </c:forEach>
                </tbody>
            </table>
        </div>
	</div>
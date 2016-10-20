<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="resources/js/amq/amq_jquery_adapter.js"></script>
<script type="text/javascript" src="resources/js/amq/amq.js"></script>
<script type="text/javascript" src="resources/js/amq/syncUtils.js"></script>
<script type="text/javascript" src="resources/js/responsibility/responsibility.js"></script>
<script type="text/javascript">
	USER = <c:out value="${sessionScope.user}"/>;
	GROUP = <c:out value="${sessionScope.group}"/>;
	ARCHITECTURE = <c:out value="${sessionScope.architecture}"/>;
	LAST_CHANGE_NUMBER = <c:out value="${lastChangeNumber}"/>;
</script>
<input type="hidden" id="idDelete" />
<div id="dialogNew"></div>
<div id="dialogEdit"></div>
<div id="dialogEditScenarios"></div>
<div id="dialogEditDependencies"></div>
<div id="dialogDetail"></div>
<div id="dialogDelete"><p style="font-size : 11px;">Are you sure you want to delete this responsibility?  </p></div>
<div id="top-panel">
    <div id="panel">
        <ul>
			<li>
				<a id="newResponsibility" href="#"><img
					src="resources/images/icons/add.png" /> New Responsibility
				</a>
			</li>
		</ul>
     </div>
</div>
    <div id="content">
        <div id="box">
            <h3>Responsibilities</h3>
            <table class="tableScenarios">
                <thead>
                    <tr >
                        <th width="40px"><a href="#">ID<img src="resources/images/icons/arrow_down_mini.gif" width="16" height="16" /></a></th>
                        <th width="90px"><a href="#">Name</a></th>
                        <th width="90px"><a href="#">Module</a></th>
                        <th width="90px"><a href="#">Complexity</a></th>
                        <th width="90px"><a href="#">Execution Time (ms)</a></th>
                        <th width="75px"></th>
                    </tr>
                </thead>
                <tbody id="tableBody">
                	<c:forEach items="${responsibilities}" var="r">
                		<jsp:include page="ResponsibilityTableItem.jsp" flush="true">
							<jsp:param name="id" value="${r.id}"/>
							<jsp:param name="name" value="${r.name}"/>
							<jsp:param name="module" value="${r.module.name}"/>
							<jsp:param name="complexity" value="${r.complexity}"/>
							<jsp:param name="executionTime" value="${r.executionTime}"/>
						</jsp:include>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
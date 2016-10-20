<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="resources/js/amq/amq_jquery_adapter.js"></script>
<script type="text/javascript" src="resources/js/amq/amq.js"></script>
<script type="text/javascript" src="resources/js/amq/syncUtils.js"></script>
<script type="text/javascript" src="resources/js/dependency/dependency.js"></script>
<script type="text/javascript">
	USER = <c:out value="${sessionScope.user}"/>;
	GROUP = <c:out value="${sessionScope.group}"/>;
	ARCHITECTURE = <c:out value="${sessionScope.architecture}"/>;
	LAST_CHANGE_NUMBER = <c:out value="${lastChangeNumber}"/>;
</script>
<div id="dialogNew"></div>
<div id="dialogEdit"></div>
<jsp:include page="DeleteDependency.jsp" flush="true"/>
<div id="top-panel">
    <div id="panel">
        <ul>
			<li>
				<a id="newDependencyButton" href="#">
					<img src="resources/images/icons/add.png" /> New Dependency
				</a>
			</li>
		</ul>
     </div>
</div>
    <div id="content">
        <div id="box">
            <h3>Dependencies</h3>
            <table class="tableScenarios">
                <thead>
                    <tr >
                    	<th width="90px" ><a href="#">Parent</a></th>
                   		<th width="90px" ><a href="#">Child</a></th>
                        <th width="90px"><a href="#">Coupling Cost</a></th>
                        <th width="75px"></th>
                    </tr>
                </thead>
                <tbody id="tableBody">
                	<c:forEach items="${dependencies}" var="d">
                		<jsp:include page="DependencyTableItem.jsp" flush="true">
                			<jsp:param name="idChild" value="${d.id.child}"/>
							<jsp:param name="idParent" value="${d.id.parent}"/>
							<jsp:param name="childName" value="${d.getChildResponsibility().name}"/>
							<jsp:param name="parentName" value="${d.getParentResponsibility().name}"/>
							<jsp:param name="couplingcost" value="${d.couplingcost}"/>
						</jsp:include>
                    </c:forEach>
                </tbody>
            </table>
        </div>
	</div>
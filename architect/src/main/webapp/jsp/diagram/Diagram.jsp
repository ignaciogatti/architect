<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- <script type="text/javascript" src="resources/js/amq/amq_jquery_adapter.js"></script> -->
<!-- <script type="text/javascript" src="resources/js/amq/amq.js"></script> -->
<!-- <script type="text/javascript" src="resources/js/amq/syncUtils.js"></script> -->
<script type="text/javascript" src="resources/js/diagram/diagram.js"></script>
<script type="text/javascript">
	USER = <c:out value="${sessionScope.user}"/>;
	GROUP = <c:out value="${sessionScope.group}"/>;
	ARCHITECTURE = <c:out value="${sessionScope.architecture}"/>;
</script>
<div id="top-panel">
    <div id="panel">
        <ul>
<!--             <li><a id="newModule" href="#"><img src="resources/images/icons/add.png" /> New Module</a></li> -->
         </ul>
     </div>
</div>
    <div id="content">
        <div id="box">
            <h3>Diagram</h3>
            <jsp:include page="diagrameditor.jsp" flush="true"/>
        </div>
    </div>
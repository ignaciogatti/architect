<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link type="text/css" rel="stylesheet" href="resources/js/plugins/dropdown/style.css" />
<link type="text/css" rel="stylesheet" href="resources/css/analysisPanel.css" />
<link type="text/css" rel="stylesheet" href="resources/css/style.css" />
<script type="text/javascript" src="resources/js/plugins/dropdown/dropdown.js"></script>
<script type="text/javascript" src="resources/js/amq/amq_jquery_adapter.js"></script>
<script type="text/javascript" src="resources/js/amq/amq.js"></script>
<script type="text/javascript" src="resources/js/amq/syncUtils.js"></script>
<script type="text/javascript" src="resources/js/analysispanel/syncOutput.js"></script>
<script type="text/javascript" src="resources/js/analysispanel/analysisPanel.js"></script>
<script type="text/javascript">
	USER = <c:out value="${sessionScope.user}"/>;
	GROUP = <c:out value="${sessionScope.group}"/>;
	ARCHITECTURE = <c:out value="${sessionScope.architecture}"/>;
	LAST_CHANGE_NUMBER = <c:out value="${lastChangeNumber}"/>;
</script>
<input type="hidden" id="idDelete" />
<div id="dialogNew"></div>
<div id="dialogEdit"></div>
<div id="dialogDelete"><p style="font-size : 11px;">Are you sure you want to delete this configuration?</p></div>
<div id="dialogChanges"></div>
<div id="dialogRunFullAnalysis"></div>
<div id="top-panel">
    <div id="panel">
        <ul>
			<li>
				<a id="newArchitectureAnalysis" href="#"><img
					src="resources/images/icons/add.png" /> New Analysis Configuration
				</a>
			</li>
		</ul>
     </div>
</div>
<div id="content">
    <div id="box">
        <h3>Analysis Configurations</h3>
		<table class="tableScenarios">
			<thead>
				<tr>
					<th width="30px"><a href="#">ID<img
							src="resources/images/icons/arrow_down_mini.gif" width="16"
							height="16" /></a></th>
					<th>Scenario</th>
					<th>Type</th>
					<th>Design Bot</th>
					<th>Enable</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody id="tableBodyAnalysis">
				<c:forEach items="${architectureAnalysis}" var="a">
					<jsp:include page="ArchitectureAnalysisTableItem.jsp" flush="true">
						<jsp:param name="id" value="${a.id}" />
						<jsp:param name="scenario" value="${a.scenario.name}" />
						<jsp:param name="type" value="${a.scenario.qualityAttribute.name}" />
						<jsp:param name="dbot" value="${a.designBot.getName()}" />
						<jsp:param name="enable" value="${a.enable}" />
					</jsp:include>
				</c:forEach>
			</tbody>
		</table>
	</div>
	
	<div id="algorithmRadios" style="margin-top: 20px;">
		<input type="radio" id="algorithmRadio1" name="algorithmRadio" value="predictive" checked/>
		<label for="algorithmRadio1">Predictive (Fast)</label><br>
		<input type="radio" id="algorithmRadio2" name="algorithmRadio" value="backtracking" />
		<label for="algorithmRadio2">Backtracking (Slow)</label>
	</div>

<%-- 	<button id="blockArchitecture" class="actionButton" style="margin-top: 20px;" onClick="blockArchitecture(${sessionScope.architecture},true)"> --%>
<!-- 		<img src="resources/images/icons/bricks_gear.png" style="vertical-align :middle;" />  Block Architecture</button> -->
	<button id="unblockArchitecture" class="actionButton hidden" style="margin-top: 20px;" onClick="blockArchitecture(${sessionScope.architecture},false)">
		<img src="resources/images/icons/bricks_gear.png " style="vertical-align :middle;" />  Unblock Architecture</button>
	<button id="fullAnalysis" class="actionButton" style="margin-top: 20px;">
		<img src="resources/images/icons/bricks_gear.png" style="vertical-align :middle;" />  Run Full Analysis
	</button>
	<button id="cleanAllAnalisys" class="actionButton" style="margin-top: 20px;" onClick="cleanAllAnalisys()">
		<img src="resources/images/icons/bricks_gear.png" style="vertical-align :middle;" />  Clean All Analysis</button>
	<div class="boxTitle">
		<h3><a href="#solutionsView" class="outputButton">
			<img style="vertical-align : middle;" class="menosSol" src="resources/images/menos_icon.png">
			<img style="vertical-align : middle; display: none;" class="masSol" src="resources/images/mas_icon.png"> Output</a>
		</h3>
	</div>
	<div class="solutionResults" id="solutionsView">
		<jsp:include page="AnalysisResult.jsp" flush="true"></jsp:include>
	</div>
</div>
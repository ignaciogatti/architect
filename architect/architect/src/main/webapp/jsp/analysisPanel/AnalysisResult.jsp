<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<table class="tableScenarios analysisResultsTable">
		<thead>
			<tr id="tableAnalysisResultsColumns">
				<th>Scenario</th>
				<th>Actual</th>
			</tr>
		</thead>
		<tbody id="tableAnalysisResultsBody">
			<c:forEach items="${actualArchitectureResults.scenarioResults}" var="s">
				<tr id="trAnalysisResult${s.scenario.id}">
					<td class="a-left results-margin name${s.scenario.id}">${s.scenario.name}</td>
					<td class="a-left results-margin value${s.scenario.id}">
						<c:choose>
							<c:when test="${s.scenarioCost > 9999}">
								<c:out value="--"></c:out>
							</c:when>
							<c:otherwise>
								<c:out value="${s.scenarioCost}"></c:out>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<button id="viewChanges" class="actionButton hidden" style="margin-top: 20px;" onClick="viewChanges()">
		<img src="resources/images/icons/bricks_gear.png" style="vertical-align :middle;"/>  View Changes and Apply</button>
	<select name="designBotToApply" id="designBotToApply" class="hidden"></select>
</div>
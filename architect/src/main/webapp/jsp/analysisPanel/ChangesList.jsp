<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="dialogContainer">
	<input type="hidden" name="idDesignBotChanges" id="idDesignBotChanges"  value="${designBotResults.architectureAnalysis.id}" />
	<c:forEach items="${designBotResults.changes}" var="tactic">
		<div class="itemDialog">
			<div class="itemDialogLeft">
				<label for="tacticName">
					<img id="closedTactic${tactic.id}" style="vertical-align : middle;" src="resources/images/mas_icon.png"  onClick="toggleTacticBlock(${tactic.id})">
					<img id="collapsedTactic${tactic.id}" style="vertical-align : middle; display: none;" src="resources/images/menos_icon.png" onClick="toggleTacticBlock(${tactic.id})">
					Tactic : 
				</label>
			</div>
			<div class="itemDialogRight">
				<span id="tacticName">${tactic.tacticName}</span>
			</div>
		</div>
		<div id="tacticBlock${tactic.id}" style="display: none;">
			<c:forEach items="${tactic.appliedChanges}" var="change">
				<div class="itemDialog">
					<div class="itemDialogLeft">
						<label for="changeName${change.id}">
							<img id="closed${tactic.id}-${change.id}" style="vertical-align : middle;" src="resources/images/mas_icon.png"  onClick="toggleItemBlock(${tactic.id},${change.id})">
							<img id="collapsed${tactic.id}-${change.id}" style="vertical-align : middle; display: none;" src="resources/images/menos_icon.png" onClick="toggleItemBlock(${tactic.id},${change.id})">
						</label>
					</div>
					<div class="itemDialogRight">
						<span id="changeName${change.id}">${change.change_type} ${change.element_type} ${change.newElement.id.toString()}</span>
					</div>
					<div id="itemBlock${tactic.id}-${change.id}" style="display: none; float:left; width:70%;">
						<c:choose>
							<c:when test="${change.element_type == 'RESPONSIBILITY'}">
								<div style="margin: 2px;">
									<label for="name">Name : </label>
									<input name="name" id="name${tactic.id}-${change.id}" type="text" value=""/>
								</div>
								<div style="margin: 2px;">
									<label for="complexity">Complexity : </label>
									<input id="complexity${tactic.id}-${change.id}" name="complexity" type="text" value=""/>
								</div>
								<button id="saveChange" class="actionButton" onClick="updateResponsibilityChange(${tactic.id},${change.id})">Save</button>
    						</c:when>
							<c:when test="${change.element_type == 'DEPENDENCY'}">
								<div style="margin: 2px;">
									<label for="couplingCost">Coupling Cost : </label>
									<input id="couplingCost${tactic.id}-${change.id}" name="couplingCost" type="text" value=""/>
								</div>
								<button id="saveChange" class="actionButton" onClick="updateDependencyChange(${tactic.id},${change.id})">Save</button>
    						</c:when>
    						<c:when test="${change.element_type == 'MODULE'}">
								<div style="margin: 2px;">
									<label for="name">Name : </label>
									<input name="name" id="name${tactic.id}-${change.id}" type="text" value=""/>
								</div>
								<button id="saveChange" class="actionButton" onClick="updateModuleChange(${tactic.id},${change.id})">Save</button>
    						</c:when>
    						<c:when test="${change.element_type == 'SCENARIO'}">
								<div style="margin: 2px;">
									<label for="period">Period : </label>
									<input name="period" id="period${tactic.id}-${change.id}" type="text" value=""/>
								</div>
								<button id="saveChange" class="actionButton" onClick="updateScenarioChange(${tactic.id},${change.id})">Save</button>
    						</c:when>
							<c:otherwise>
    						</c:otherwise>
						</c:choose>
					</div>
				</div>
			</c:forEach>
		</div>
	</c:forEach>
</div>
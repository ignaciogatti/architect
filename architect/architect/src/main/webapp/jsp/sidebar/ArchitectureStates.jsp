<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="sideBarOptionField" >
	<select name="changes" id="changes" >
		<c:forEach items="${architctureStates}" var="s">
        	<option value="${s.id}">${s.id} - ${s.element_type} ${s.change_type}</option>
        </c:forEach>
	</select>	
</div>
<div class="sideBarOptionField">
	<img src="resources/images/icons/refresh.png" title="Refresh" onClick="refreshArchitectureStates()"/>
	<button id="applyUndo" class="actionButton hidden" onClick="applyUndo()">
	<img src="resources/images/icons/bricks_gear.png" style="vertical-align :middle;"/>  Apply Undo</button>
</div>
